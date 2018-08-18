import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
public class ProgrammerCalcMain extends Application
{
	public static final String[] BUTTONS_LIST = {"Lsh", "Rsh", "Or", "Xor", "Not", "And",
												"^", "Mod", "CE", "C", "Del", "/",  //using setDisable(bool);
												"A", "B", "7", "8", "9", "X",	   //entire first row + first col of second row
												"C", "D", "4", "5", "6", "-",
												"E", "F", "1", "2", "3", "+",
												"(", ")", "±", "0", ".", "="};
	public static final int numCols = 6;
	public static final int numRows = 6;
	boolean clearNext = false; //used after calculations to let calculator that the real value is 0.
	boolean equalsPressed = false;
	int mode = 1; //0 is hex, 1 is decimal, 2 is octal and 3 is binary
	long calcDecValue = 0; //value for calc, tied to Dec.
	String operation = ""; //5 states, empty, /, x, +, -
	Deque<String> equationStack = new ArrayDeque<String>();
	Button[] calcList = new Button[BUTTONS_LIST.length]; //used for buttons
	GridPane buttonsPane = new GridPane();  //pane used for layout of buttons, grid pane does a grid setup, might use flowpane
	Pane calcPane = new Pane(); //Text element in new pane, for numbers part of calc
	Text calText = new Text(700, 150, ""); //will be a blown up copy of hexText, decText, octText or binText
	Text curText = new Text(700, 90, ""); //text above calculator in win 10 calc
	Button decTextButton = new Button("");
	Text decText = new Text(10, 350, "Dec: "); //displays decimal
	Button hexTextButton = new Button("");
	Text hexText = new Text(10, 300, "Hex: "); //displays hex numbers
	Button octTextButton = new Button("");
	Text octText = new Text(10, 400, "Oct: "); //displays octal
	Button binTextButton = new Button("");
	Text binText = new Text(10, 450, "Bin: "); //displays binary
/*  Data type	Size of data type (bits)	Lower limit					Upper limit
	Byte		8							-128						127
	Word		16							-32,768						32,767
	Dword		32							-2,147,483,648				2,147,483,647
	Qword		64							-9,223,372,036,854,775,808	9,223,372,036,854,775,807
*/
	public static void main(String args[])
	{
		Application.launch(args);
	}
	
	public void start(Stage primary)
	{
		primary.setResizable(false); //stage can no longer be resized by user
		
		//setting up buttons pane
		buttonsPane.setPadding(new Insets(-100, 10, 10, 10)); //puts top of pane in bottom middle section of screen
		buttonsPane.setHgap(3); //gap between elements in row
		buttonsPane.setVgap(3); //gap between column 
		
		//setting up calc pane
		
		calcPane.setPadding(new Insets(150, 10, 10, 10));
		
		//setting up styles for text objects
		
		calText.setId("calcText");
		curText.setId("equText");
		//button/text pairs setup
		hexTextButton.setId("convTextButtonUnhovered");
		hexTextButton.setLayoutY(275);
		hexText.setId("convText");
		
		decTextButton.setId("convTextButtonUnhovered");
		decTextButton.setLayoutY(325);
		decText.setId("convText");
		
		octTextButton.setId("convTextButtonUnhovered");
		octTextButton.setLayoutY(375);
		octText.setId("convText");
		
		binTextButton.setId("convTextButtonUnhovered");
		binTextButton.setLayoutY(425);
		binText.setId("convText");
		
		//set buttons, row col order using BUTTONS_LIST
		for(int j = 0; j < numRows; j++) //row
		{
			for(int k = 0; k < numCols; k++) //col
			{
				calcList[j*6 + k] = new Button(BUTTONS_LIST[j*6 + k]); //math
				GridPane.setConstraints(calcList[j*6 + k], k, j);
			}
		}
		//disable unusable/unused buttons
		calcList[0].setDisable(true); //disables Lsh
		calcList[1].setDisable(true); //disables Rsh
		calcList[2].setDisable(true); //disables Or
		calcList[3].setDisable(true); //disables Xor
		calcList[4].setDisable(true); //disables Not
		calcList[5].setDisable(true); //disables And
		calcList[6].setDisable(true); //disables ^
		calcList[34].setDisable(true); //disables .
		
		
		hexTextButton.setOnMouseEntered(e -> hexTextButton.setId("convTextButtonHovered"));
		hexTextButton.setOnMouseExited(e -> hexTextButton.setId("convTextButtonUnhovered"));
		
		decTextButton.setOnMouseEntered(e -> decTextButton.setId("convTextButtonHovered"));
		decTextButton.setOnMouseExited(e -> decTextButton.setId("convTextButtonUnhovered"));
		
		octTextButton.setOnMouseEntered(e -> octTextButton.setId("convTextButtonHovered"));
		octTextButton.setOnMouseExited(e -> octTextButton.setId("convTextButtonUnhovered"));
		
		binTextButton.setOnMouseEntered(e -> binTextButton.setId("convTextButtonHovered"));
		binTextButton.setOnMouseExited(e -> binTextButton.setId("convTextButtonUnhovered"));
		//button function declaration
		//actions for 1-9 buttons in order
		calcList[26].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("1");
		});
		calcList[27].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("2");
		});
		calcList[28].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("3");
		});
		calcList[20].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("4");
		});
		calcList[21].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("5");
		});
		calcList[22].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("6");
		});
		calcList[14].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("7");
		});
		calcList[15].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("8");
		});
		calcList[16].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("9");
		});
		calcList[33].setOnAction((ActionEvent e)-> 
		{
			numberButtonPressed("0");
		});
		
		//letter buttons A-F
		calcList[12].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("A");
		});
		calcList[13].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("B");
		});
		calcList[18].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("C");
		});
		calcList[19].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("D");
		});
		calcList[24].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("E");
		});
		calcList[25].setOnAction((ActionEvent e)->
		{
			numberButtonPressed("F");
		});
		
		//operation buttons
		calcList[11].setOnAction((ActionEvent e)-> //division
		{
			divide();
		});
		calcList[17].setOnAction((ActionEvent e)-> //multiplication
		{
			multiply();
		});
		calcList[23].setOnAction((ActionEvent e)->  //subtraction
		{
			subtract();
		});
		calcList[29].setOnAction((ActionEvent e)-> //addition
		{
			addition();
		});
		calcList[35].setOnAction((ActionEvent e)-> //equals
		{
			equals();
		});
		calcList[7].setOnAction((ActionEvent e)-> //modulus
		{
			modulus();
		});
		
		calcList[30].setOnAction((ActionEvent e)-> //Left parentheses
		{
			openParen();
		});
		calcList[31].setOnAction((ActionEvent e)-> //right parentheses
		{
			closeParen();
		});
		calcList[32].setOnAction((ActionEvent e)-> //plus/minus
		{
			toggleSign();
		});
		
		//CE/Clear/Del
		calcList[8].setOnAction((ActionEvent e)-> //Clear Entry
		{
			clearEntry();
		});
		calcList[9].setOnAction((ActionEvent e)-> //Clear
		{
			clearAll();
		});
		calcList[10].setOnAction((ActionEvent e)-> //Del
		{
			deleteOne();
		});
		
		//mode changing buttons (i.e. change to hex display)
		hexTextButton.setOnAction((ActionEvent e)->
		{
			switchMode(0);
		});
		decTextButton.setOnAction((ActionEvent e)->
		{
			switchMode(1);
		});
		octTextButton.setOnAction((ActionEvent e)->
		{
			switchMode(2);
		});
		binTextButton.setOnAction((ActionEvent e)->
		{
			switchMode(3);
		});
		
		//add to panes
		buttonsPane.getChildren().addAll(calcList);
		calcPane.getChildren().addAll(calText, hexTextButton, hexText, decTextButton, decText,  octTextButton, octText,  binTextButton, binText, curText);
		
		//setup scene using VBox
		Scene calcBase = new Scene(new VBox(calcPane, buttonsPane), 700, 1000);
		//set style sheet and display
		calcBase.getStylesheets().add("calcSheet.css"); //set our style sheet so i don't have to do each button
		primary.setTitle("Programming Calculator");
		primary.setScene(calcBase);
		primary.show();
		switchMode(1);
		numberButtonPressed("0");
	}
	

	private void addition() 
	{
		if(equalsPressed)
		{
			equalsPressed = false;
			operation = "+";
			equationStack.push("+");
			curText.setText(curText.getText() + " + ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			
		}
		if(calcDecValue != 0)
		{
			operation = "+";
			equationStack.push(""+calcDecValue);
			equationStack.push("+");
			curText.setText(curText.getText() + calText.getText() + " + ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
		}
		else if(equationStack.peek() != null && (equationStack.peek().equals("-") || equationStack.peek().equals("x") || equationStack.peek().equals("/") || equationStack.peek().equals("%")))
		{
			equationStack.pop();
			equationStack.push("+");
			curText.setText(curText.getText().substring(0, curText.getText().length()-2) + "+ ");
		}
		if(equationStack.size() >= 4 && (equationStack.peek().equals("+") || equationStack.peek().equals("-"))) //i.e. + 22 + 44 which in terms of buttons means 4 4 + 2 2 +
		{
			doCalculation(true);
		}
	}

	private void subtract() 
	{
		if(equalsPressed)
		{
			equalsPressed = false;
			operation = "-";
			equationStack.push("-");
			curText.setText(curText.getText() + " - ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			
		}
		if(calcDecValue != 0)
		{
			operation = "-";
			equationStack.push(""+calcDecValue);
			equationStack.push("-");
			curText.setText(curText.getText() + calText.getText() + " - ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
		}
		else if(equationStack.peek() != null && (equationStack.peek().equals("+") || equationStack.peek().equals("x") || equationStack.peek().equals("/") || equationStack.peek().equals("%")))
		{
			equationStack.pop();
			equationStack.push("-");
			curText.setText(curText.getText().substring(0, curText.getText().length()-2) + "- ");
		}
		if(equationStack.size() >= 4 && (equationStack.peek().equals("+") || equationStack.peek().equals("-"))) //i.e. + 22 + 44 which in terms of buttons means 4 4 + 2 2 +
		{
			doCalculation(true);
		}
	}

	private void multiply() 
	{
		if(equalsPressed)
		{
			equalsPressed = false;
			operation = "x";
			equationStack.push("x");
			curText.setText(curText.getText() + " x ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			
		}
		if(calcDecValue != 0)
		{
			operation = "x";
			equationStack.push(""+calcDecValue);
			equationStack.push("x");
			curText.setText(curText.getText() + calText.getText() + " x ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
		}
		else if(equationStack.peek() != null && (equationStack.peek().equals("+") || equationStack.peek().equals("-") || equationStack.peek().equals("/") || equationStack.peek().equals("%")))
		{
			equationStack.pop();
			equationStack.push("x");
			curText.setText(curText.getText().substring(0, curText.getText().length()-2) + "x ");
		}
		if(equationStack.size() >= 4 && (equationStack.peek().equals("+") || equationStack.peek().equals("-"))) //i.e. + 22 + 44 which in terms of buttons means 4 4 + 2 2 +
		{
			doCalculation(true);
		}
	}
	
	private void modulus()
	{
		if(equalsPressed)
		{
			equalsPressed = false;
			operation = "%";
			equationStack.push("%");
			curText.setText(curText.getText() + " % ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			
		}
		if(calcDecValue != 0)
		{
			operation = "%";
			equationStack.push(""+calcDecValue);
			equationStack.push("%");
			curText.setText(curText.getText() + " % ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
		}
		else if(equationStack.peek() != null && (equationStack.peek().equals("+") || equationStack.peek().equals("-") || equationStack.peek().equals("/") || equationStack.peek().equals("x")))
		{
			equationStack.pop();
			equationStack.push("%");
			curText.setText(curText.getText().substring(0, curText.getText().length()-2) + "% ");
		}
		if(equationStack.size() >= 4 && (equationStack.peek().equals("+") || equationStack.peek().equals("-"))) //i.e. + 22 + 44 which in terms of buttons means 4 4 + 2 2 +
		{
			doCalculation(true);
		}
	}

	private void divide() 
	{
		if(equalsPressed)
		{
			equalsPressed = false;
			operation = "/";
			equationStack.push("/");
			curText.setText(curText.getText() + " / ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			
		}
		if(calcDecValue != 0)
		{
			operation = "/";
			equationStack.push(""+calcDecValue);
			equationStack.push("/");
			curText.setText(curText.getText() + calText.getText() + " / ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
		}
		else if(equationStack.peek() != null && (equationStack.peek().equals("+") || equationStack.peek().equals("x") || equationStack.peek().equals("-")))
		{
			equationStack.pop();
			equationStack.push("/");
			curText.setText(curText.getText().substring(0, curText.getText().length()-2) + "/ ");
		}
		if(equationStack.size() >= 4 && (equationStack.peek().equals("+") || equationStack.peek().equals("-"))) //i.e. + 22 + 44 which in terms of buttons means 4 4 + 2 2 +
		{
			doCalculation(true);
		}
	}
	
	private void equals()
	{
		
		if(equationStack.size()+1 >= 3) //stack size of 2 is simply a number + operator, the + represents the number still in calc
		{
			equationStack.push(""+calcDecValue);
			curText.setText(curText.getText() + calText.getText() + " + ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			equalsPressed = true;
			doCalculation(false);
		}
		if(equationStack.size() == 3)
		{
			curText.setText(curText.getText() + calText.getText() + " + ");
			curText.setTranslateX(-12*curText.getText().length());
			resetCalc(); //calls update calText too
			equalsPressed = true;
			doCalculation(false);
		}
		
	}
	
	private void doCalculation(boolean doSavePop) 
	{
		long result = 0;
		String savePop = "";
		if(doSavePop)
		{
			savePop = equationStack.pop();
		}
		long number2 = Long.parseLong(equationStack.pop());
		String operation = equationStack.pop();
		long number1 = Long.parseLong(equationStack.pop());
		if(operation.equals("+"))
		{
			result = number1 + number2;
		}
		else if(operation.equals("-"))
		{
			result = number1 - number2;
		}
		else if(operation.equals("x"))
		{
			result = number1 * number2;
		}
		else if(operation.equals("/"))
		{
			result = number1 / number2;
		}
		else if(operation.equals("%"))
		{
			result = number1 % number2;
		}
		clearNext = true;
		hexText.setText("Hex: " + Long.toHexString(result));
		decText.setText("Dec: " + result);
		octText.setText("Oct: " + Long.toOctalString(result));
		binText.setText("Bin: " + Long.toBinaryString(result));
		updateCalText(); //syncs cal text
		String[] saveStack = new String[equationStack.size()];
		for(int i = equationStack.size()-1; i >= 0; i--)
		{
			saveStack[i] = equationStack.pop();
		}
		curText.setText("");
		for(int i = 0; i < saveStack.length; i++)
		{
			curText.setText(curText.getText() + saveStack[i] + " ");
		}
		for(int i = 0; i < saveStack.length; i++)
		{
			equationStack.push(saveStack[i]);
		}
		curText.setText(curText.getText() + "" + result);
		
		equationStack.push(""+result);
		if(doSavePop)
		{
			equationStack.push(savePop);
			curText.setText(curText.getText() + " " + savePop + " ");
		}
		if(equationStack.size() >= 4)
		{
			doCalculation(true);
		}
		curText.setTranslateX(-12*curText.getText().length());
	}
	
	private void switchMode(int m) 
	{
		mode = m;
		updateCalText();
		//hex
		if(m == 0)
		{
			calcList[27].setDisable(false); //2 - 9
			calcList[28].setDisable(false);
			calcList[20].setDisable(false);
			calcList[21].setDisable(false);
			calcList[22].setDisable(false);
			calcList[14].setDisable(false);
			calcList[15].setDisable(false);
			calcList[16].setDisable(false);
			calcList[12].setDisable(false); //A - F
			calcList[13].setDisable(false); 
			calcList[18].setDisable(false);
			calcList[19].setDisable(false);
			calcList[24].setDisable(false);
			calcList[25].setDisable(false);
			calcList[32].setDisable(true); //plus/minus
		}
		//dec
		else if(m == 1)
		{
			calcList[27].setDisable(false); //2 - 9
			calcList[28].setDisable(false);
			calcList[20].setDisable(false);
			calcList[21].setDisable(false);
			calcList[22].setDisable(false);
			calcList[14].setDisable(false);
			calcList[15].setDisable(false);
			calcList[16].setDisable(false);
			calcList[32].setDisable(false); //plus/minus
			calcList[12].setDisable(true); //A - F
			calcList[13].setDisable(true); 
			calcList[18].setDisable(true);
			calcList[19].setDisable(true);
			calcList[24].setDisable(true);
			calcList[25].setDisable(true);
		}
		//oct
		else if(m == 2)
		{
			calcList[27].setDisable(false); //2 - 9
			calcList[28].setDisable(false);
			calcList[20].setDisable(false);
			calcList[21].setDisable(false);
			calcList[22].setDisable(false);
			calcList[14].setDisable(false);
			calcList[15].setDisable(true);
			calcList[16].setDisable(true);
			calcList[12].setDisable(true); //A - F
			calcList[13].setDisable(true); 
			calcList[18].setDisable(true);
			calcList[19].setDisable(true);
			calcList[24].setDisable(true);
			calcList[25].setDisable(true);
			calcList[32].setDisable(true); //plus/minus
		}
		//bin
		else if(m == 3)
		{
			calcList[27].setDisable(true); //2 - 9
			calcList[28].setDisable(true);
			calcList[20].setDisable(true);
			calcList[21].setDisable(true);
			calcList[22].setDisable(true);
			calcList[14].setDisable(true);
			calcList[15].setDisable(true);
			calcList[16].setDisable(true);
			calcList[12].setDisable(true); //A - F
			calcList[13].setDisable(true); 
			calcList[18].setDisable(true);
			calcList[19].setDisable(true);
			calcList[24].setDisable(true);
			calcList[25].setDisable(true);
			calcList[32].setDisable(true); //plus/minus
		}
	}
	
	private void openParen()
	{
		curText.setText(curText.getText() + " (");
		curText.setTranslateX(-12*curText.getText().length());
		equationStack.push("(");
		equationStack.pop();
	}
	private void closeParen()
	{
		curText.setText(curText.getText() + ") ");
		curText.setTranslateX(-12*curText.getText().length());
		equationStack.push(")");
		equationStack.pop();
	}
	
	private void toggleSign()
	{
		calcDecValue*=-1;
		hexText.setText("Hex: " + Long.toHexString(calcDecValue));
		decText.setText("Dec: " + calcDecValue);
		octText.setText("Oct: " + Long.toOctalString(calcDecValue));
		binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		updateCalText();
	}
	
	private void deleteOne()
	{
		if(calcDecValue == 0)
		{
			return;
		}
		if(mode == 0)
		{
			hexText.setText(hexText.getText().substring(0, hexText.getText().length()-1));
			if(hexText.getText().equals("Hex: "))
			{
				hexText.setText("Hex: 0");
			}
			calcDecValue = Long.parseLong(hexText.getText().substring(5), 16);
			decText.setText("Dec: " + calcDecValue);
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 1)
		{
			decText.setText(decText.getText().substring(0, decText.getText().length()-1));
			if(decText.getText().equals("Dec: "))
			{
				decText.setText("Dec: 0");
			}
			calcDecValue = Long.parseLong(decText.getText().substring(5));
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 2)
		{
			octText.setText(octText.getText().substring(0, octText.getText().length()-1));
			if(octText.getText().equals("Oct: "))
			{
				octText.setText("Oct: 0");
			}
			calcDecValue = Long.parseLong(octText.getText().substring(5), 8);
			decText.setText("Dec: " + calcDecValue);
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 3)
		{
			binText.setText(binText.getText().substring(0, binText.getText().length()-1));
			if(binText.getText().equals("Bin: "))
			{
				binText.setText("Bin: 0");
			}
			calcDecValue = Long.parseLong(binText.getText().substring(5), 2);
			decText.setText("Dec: " + calcDecValue);
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
		}
		updateCalText();
	}
	
	private void clearAll()
	{
		equalsPressed = false;
		equationStack = new ArrayDeque<String>();
		curText.setText("");
		resetCalc();
	}
	private void clearEntry()
	{
		if(clearNext)
		{
			clearAll();
			return;
		}
		resetCalc();
	}

	private void numberButtonPressed(String s)
	{
		if(mode != 0 && (Integer.parseInt(s) == 0 && calText.getText().equals("0")))
		{
			return;
		}
		else if(mode == 0 && (Long.parseLong(s, 16) == 0) && calText.getText().equals("0"))
		{
			return;
		}
		if(calText.getText().equals("0") || clearNext)
		{
			hexText.setText("Hex: ");
			decText.setText("Dec: ");
			octText.setText("Oct: ");
			binText.setText("Bin: ");
			clearNext = false;
		}
		
		//making sure the s is added on properly, to the right one and then values are updated to match
		if(mode == 0) //hex
		{
			hexText.setText(hexText.getText() + s);
			calcDecValue = Long.parseLong(hexText.getText().substring(5), 16);
			decText.setText("Dec: " + calcDecValue);
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 1) //dec
		{
			decText.setText(decText.getText()+s); //do dec text first, simple add
			calcDecValue = Long.parseLong(decText.getText().substring(5));
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 2) //oct
		{
			octText.setText(octText.getText() + s);
			calcDecValue = Long.parseLong(octText.getText().substring(5), 8);
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
			decText.setText("Dec: " + calcDecValue);
			binText.setText("Bin: " + Long.toBinaryString(calcDecValue));
		}
		else if(mode == 3) //bin
		{
			binText.setText(binText.getText() + s);
			calcDecValue = Long.parseLong(binText.getText().substring(5), 2);
			hexText.setText("Hex: " + Long.toHexString(calcDecValue));
			octText.setText("Oct: " + Long.toOctalString(calcDecValue));
			decText.setText("Dec: " + calcDecValue);
		}
		//call updateCalText to get caltext right
		updateCalText();
	}
	
	private void updateCalText()
	{
		if(mode == 0) //hex
		{
			calText.setText(hexText.getText().substring(5));
			calText.setTranslateX(-35*calText.getText().length());
		}
		else if(mode == 1) //dec
		{
			calText.setText(decText.getText().substring(5));
			calText.setTranslateX(-35*calText.getText().length());
		}
		else if(mode == 2) //oct
		{
			calText.setText(octText.getText().substring(5));
			calText.setTranslateX(-35*calText.getText().length());
		}
		else if(mode == 3) //binary
		{
			calText.setText(binText.getText().substring(5));
			calText.setTranslateX(-35*calText.getText().length());
		}
	}
	
	private void resetCalc()
	{
		calcDecValue = 0;
		hexText.setText("Hex: ");
		decText.setText("Dec: ");
		octText.setText("Oct: ");
		binText.setText("Bin: ");
		calText.setText("");
		numberButtonPressed("0");
	}
}
