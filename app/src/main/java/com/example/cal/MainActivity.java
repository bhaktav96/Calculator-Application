package com.example.cal;


import android.app.Activity;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {
    //Defining variables.
    TextView value, ans;
    // Temporary storing values.
    String svalue = "", sans = "", num_one = "", num_two = "", cur_oprator = "", prev_ans = "";
    //Initialising the values at start
    Double Result = 0.0, numberOne = 0.0, numberTwo = 0.0, temp = 0.0;
    Boolean dot_present = false, number_allow = true, root_pres = false, invert_allow = true, pow_pres = false;
    Boolean fact_pres = false, func_pres = false, val_invert = false;
    //we need to reformat ans
    NumberFormat format, longfrte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = findViewById(R.id.value);
        //set movement to the text view
        value.setMovementMethod(new ScrollingMovementMethod());
        //initialize ans
        ans = findViewById(R.id.ans);

        //we set the ans upto four decimal
        format = new DecimalFormat("#.####");
        //we need to reformat ans if it's long
        longfrte = new DecimalFormat("0.#E0");

    }

    public void onClickNumber(View v) {
        //we need to find which button is pressed
        if (number_allow) {
            Button bn = (Button) v;
            svalue += bn.getText();
            num_one += bn.getText();
            numberOne = Double.parseDouble(num_one);


            //check root is present
            if (root_pres) {
                numberOne = Math.sqrt(numberOne);
            }
            switch (cur_oprator) {

                case ""://if current oprator is null
                    // checking for power present.
                    if (pow_pres) {
                        temp = Result + Math.pow(numberTwo, numberOne);//storing result in temp.
                    } else {
                        temp = Result + numberOne;
                    }
                    break;

                case "+":// if current operator is +
                    if (pow_pres) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result + numberOne;
                    }
                    break;

                case "-"://if current operator is -
                    if (pow_pres) {
                        temp = Result - Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result - numberOne;
                    }
                    break;

                case "x"://if current operator is *
                    if (pow_pres) {
                        temp = Result * Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result * numberOne;
                    }
                    break;

                case "/":
                    try {
                        // divided by 0 cause execption
                        if (pow_pres) {
                            temp = Result / Math.pow(numberTwo, numberOne);
                        } else {
                            temp = Result / numberOne;
                        }
                    } catch (Exception e) {
                        sans = e.getMessage();
                    }
                    break;

            }
            sans = format.format(temp).toString();
            updatevalue();
        }
    }

    public void onClickOprator(View v) {
        Button ob = (Button) v;
        //if sans is null means no value needed
        if (sans != "") {
            //we check last char is operator or not
            if (cur_oprator != "") {
                char c = getcharfromLast(svalue, 2);// 2 is the char from last because our las char is " "
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    svalue = svalue.substring(0, svalue.length() - 3);
                }
            }
            svalue = svalue + "\n" + ob.getText() + " ";
            num_one = "";
            Result = temp;
            cur_oprator = ob.getText().toString();// converting the button ob.getText to String
            updatevalue();
            //when operator click dot is not present in num_one
            num_two = "";
            numberTwo = 0.0;
            dot_present = false;
            number_allow = true;
            root_pres = false;
            invert_allow = true;
            pow_pres = false;
            fact_pres = false;
            val_invert = false;
        }

    }
    // Method for getting char from last.
    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }
    // on click event method used for clearing the values.
    public void onClickClear(View v) {
        cleardata();
    }
    //method used for clearing the values.
    public void cleardata() {
        //setting all variables to default values
        svalue = "";
        sans = "";
        cur_oprator = "";
        num_one = "";
        num_two = "";
        prev_ans = "";
        Result = 0.0;
        numberOne = 0.0;
        numberTwo = 0.0;
        temp = 0.0;
        updatevalue();
        dot_present = false;
        number_allow = true;
        root_pres = false;
        invert_allow = true;
        pow_pres = false;
        fact_pres = false;
        val_invert = false;
    }
    //Method for updating Value after execution.
    public void updatevalue() {
        value.setText(svalue);
        ans.setText(sans);
    }

    //Method when dot variable is clicked in input number and checking dot variable already exist in input number.
    public void onDotClick(View view) {
        //create boolean dot_present check if dot is present or not.
        if (!dot_present) {
            //check length of number one
            if (num_one.length() == 0) {
                num_one = "0.";
                svalue += "0.";
                sans = "0.";
                dot_present = true;
                updatevalue();
            } else {
                num_one += ".";
                svalue += ".";
                sans += ".";
                dot_present = true;
                updatevalue();
            }
        }

    }

    //Method for displaying the result when user clicks equals.
    public void onClickEqual(View view) {
        showresult();
    }

    // method for displaying the result after calculation
    public void showresult() {
        if (sans != "" && sans != prev_ans) {
            svalue += "\n= " + sans + "\n----------\n" + sans + " ";
            num_one = "";
            num_two = "";
            numberTwo = 0.0;
            numberOne = 0.0;
            Result = temp;
            prev_ans = sans;
            updatevalue();
            //we  don't allow to edit our ans so
            dot_present = true;
            pow_pres = false;
            number_allow = false;
            fact_pres = false;
            func_pres = false;
            val_invert = false;
        }
    }

    //Method when modulo operator is clicked.
    public void onModuloClick(View view) {
        if (sans != "" && getcharfromLast(svalue, 1) != ' ') {
            svalue += "% ";
            //value of temp will change according to the operator
            switch (cur_oprator) { // After the result if any operator is click then use switch case.
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * numberOne) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * numberOne) / 100);
                    break;
                case "x":
                    temp = Result * (numberOne / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (numberOne / 100);
                    } catch (Exception e) {
                        sans = e.getMessage();
                    }
                    break;
            }
            //Formatting the output number.
            sans = format.format(temp).toString();
            if (sans.length() > 9) { //if length >9 then long formatting
                sans = longfrte.format(temp).toString();
            }
            Result = temp;
            //now we show the result
            showresult();

        }
    }

    //Method for Plus or Minus operator is clicked.
    public void onPorMClick(View view) {//checking for invert allow i.e. true or false.
        if (invert_allow) {//if true then checking sans and svalue is empty or not
            if (sans != "" && getcharfromLast(svalue, 1) != ' ') {
                numberOne = numberOne * (-1);
                num_one = format.format(numberOne).toString();
                switch (cur_oprator) {
                    case "":
                        temp = numberOne;
                        svalue = num_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "*":
                        temp = Result * numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            //we need to add - sign in the starting of the string
                            removeuntilchar(svalue, ' ');
                            svalue += num_one;
                        } catch (Exception e) {
                            sans = e.getMessage();
                        }
                        break;
                }
                sans = format.format(temp).toString();
                val_invert = val_invert ? false : true;//Setting the conditional operator when operation is done.
                updatevalue(); // Updating the values
            }
        }
    }

    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            //remove last char
            str = removechar(str, 1);
            svalue = str;
            updatevalue();
            removeuntilchar(str, chr);
        }
    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        //we need to check if dot is removed or not
        if (c == '.' && !dot_present) {
            dot_present = false;
        }
        if (c == '^') {
            pow_pres = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }

    //Method for root function clicked
    public void onRootClick(View view) {
        Button root = (Button) view;
        //first we check if root is present or not
        if (sans == "" && Result == 0 && !root_pres && !func_pres) {
            svalue = root.getText().toString();
            root_pres = true;
            invert_allow = false;// when root is true then invert is false.
            updatevalue();
        } else if (getcharfromLast(svalue, 1) == ' ' && cur_oprator != "" && !root_pres) {
            svalue += root.getText().toString();
            root_pres = true;
            invert_allow = false;
            updatevalue();
        }
    }

    //Method for power function clicked.
    public void onPowerClick(View view) {
        Button power = (Button) view;
        if (svalue != "" && !root_pres && !pow_pres && !func_pres) {
            if (getcharfromLast(svalue, 1) != ' ') {
                svalue += power.getText().toString();
                //we need second variable for the power
                num_two = num_one;
                numberTwo = numberOne;
                num_one = "";
                pow_pres = true;
                updatevalue();
            }
        }
    }

   //Method when input number square is clicked
    public void onSquareClick(View view) {
        if (svalue != "" && sans != "") {
            if (!root_pres && !func_pres && !pow_pres && getcharfromLast(svalue, 1) != ' ' && getcharfromLast(svalue, 1) != ' ') {
                numberOne = numberOne * numberOne;
                num_one = format.format(numberOne).toString();
                if (cur_oprator == "") {
                    if (num_one.length() > 9) {
                        num_one = longfrte.format(numberOne);
                    }
                    svalue = num_one;
                    temp = numberOne;
                } else {
                    switch (cur_oprator) {// After the result if any operator is click then use switch case.
                        case "+":
                            temp = Result + numberOne;
                            break;
                        case "-":
                            temp = Result - numberOne;
                            break;
                        case "x":
                            temp = Result * numberOne;
                            break;
                        case "/":
                            try {
                                temp = Result / numberOne;
                            } catch (Exception e) {
                                sans = e.getMessage();
                            }
                            break;
                    }
                    removeuntilchar(svalue, ' ');
                    if (num_one.length() > 9) {
                        num_one = longfrte.format(numberOne);
                    }
                    svalue += num_one;
                }
                sans = format.format(temp);
                if (sans.length() > 9) {
                    sans = longfrte.format(temp);
                }
                updatevalue();
            }
        }
    }

    public void onCubeClick(View view) {
        if (svalue != "" && sans != "") {
            if (!root_pres && !func_pres && !pow_pres && getcharfromLast(svalue, 1) != ' ' && getcharfromLast(svalue, 1) != ' ') {
                numberOne = numberOne * numberOne*numberOne;
                num_one = format.format(numberOne).toString();
                if (cur_oprator == "") {
                    if (num_one.length() > 9) {
                        num_one = longfrte.format(numberOne);
                    }
                    svalue = num_one;
                    temp = numberOne;
                } else {// After the result if any operator is click then use switch case.
                    switch (cur_oprator) {
                        case "+":
                            temp = Result + numberOne;
                            break;
                        case "-":
                            temp = Result - numberOne;
                            break;
                        case "x":
                            temp = Result * numberOne;
                            break;
                        case "/":
                            try {
                                temp = Result / numberOne;
                            } catch (Exception e) {
                                sans = e.getMessage();
                            }
                            break;
                    }
                    removeuntilchar(svalue, ' ');
                    if (num_one.length() > 9) {
                        num_one = longfrte.format(numberOne);
                    }
                    svalue += num_one;
                }
                sans = format.format(temp);
                if (sans.length() > 9) {
                    sans = longfrte.format(temp);
                }
                updatevalue();
            }
        }
    }

    public void onClickFactorial(View view) {
        if (!sans.equals("") && !fact_pres && !root_pres && !dot_present && !pow_pres && !func_pres) {
            if (getcharfromLast(svalue, 1) != ' ') {
                for (int i = 1; i < Integer.parseInt(num_one); i++) {
                    numberOne *= i;
                }
                if (numberOne.equals(0.0)) {
                    numberOne = 1.0;
                }
                num_one = format.format(numberOne).toString();
                // After the result if any operator is click then use switch case.
                switch (cur_oprator) {
                    case "":
                        Result = numberOne;
                        break;
                    case "+":
                        Result += numberOne;
                        break;
                    case "-":
                        Result -= numberOne;
                        break;
                    case "x":
                        Result *= numberOne;
                        break;
                    case "/":
                        try {
                            Result /= numberOne;
                        } catch (Exception e) {
                            sans = e.getMessage();
                        }

                        break;
                }
                sans = Result.toString();
                temp = Result;
                svalue += "! ";
                fact_pres = true;
                number_allow = false; // Not allowing number to input after result.
                updatevalue();
            }
        }
    }

    //Method for number inversion
    public void onClickInverse(View view) {
        if (!sans.equals("") && !fact_pres && !root_pres && !dot_present && !pow_pres && !func_pres) {
            if (getcharfromLast(svalue, 1) != ' ') {
                numberOne = Math.pow(numberOne, -1);
                num_one = format.format(numberOne).toString();
                // After the result if any operator is click then use switch case.
                switch (cur_oprator) {
                    case "":
                        temp = numberOne;
                        svalue = num_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "x":
                        temp = Result * numberOne;
                        removeuntilchar(svalue, ' ');
                        svalue += num_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            removeuntilchar(svalue, ' ');
                            svalue += num_one;
                        } catch (Exception e) {
                            sans = e.getMessage();
                        }

                        break;
                }
                sans = format.format(temp).toString();
                updatevalue();
            }
        }
    }




}
