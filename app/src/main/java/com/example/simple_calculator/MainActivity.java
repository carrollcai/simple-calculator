package com.example.simple_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int[] numButtonIds = new int[]{
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
    };
    private int[] symbolButtonIds = new int[]{
            R.id.btnPlus, R.id.btnMinus, R.id.btnMulti, R.id.btnDivider, R.id.btnEqual, R.id.btnClear, R.id.btnDot
    };
    private String expLeft = "", expRight = "";
    private EditText editText;
    private boolean isLeftExp = true;
    private Symbols symbol;

    private enum Symbols {
        plus,
        minus,
        multi,
        divider
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton();
        editText = findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        for (int btn : numButtonIds) {
            if (btn == v.getId()) {
                handleNumber(index);
                return;
            }
            index++;
        }

        switch (v.getId()) {
            case R.id.btnPlus:
                handleOperator(Symbols.plus);
                break;
            case R.id.btnMinus:
                handleOperator(Symbols.minus);
                break;
            case R.id.btnMulti:
                handleOperator(Symbols.multi);
                break;
            case R.id.btnDivider:
                handleOperator(Symbols.divider);
                break;
            case R.id.btnEqual:
                handleEqual();
                break;
            case R.id.btnClear:
                handleClear();
                break;
            case R.id.btnDot:
                handleDot();
                break;
            default:
                break;
        }
    }

    private void handleDot() {
        if ((editText.getText() + "").contains(".")) return;
        String temp = "";
        if (isLeftExp) {
            temp = expLeft + ".";
            expLeft = temp;
        } else {
            temp = expRight + ".";
            expRight = temp;
        }
        editText.setText(temp);
    }

    private void handleClear() {
        isLeftExp = true;
        expLeft = "";
        expRight = "";
        symbol = null;
        editText.setText("");
    }

    private void handleOperator(Symbols s) {
        if (expLeft == "") return;
        isLeftExp = false;
        symbol = s;
    }

    private void handleNumber(int index) {
        String temp = "";
        if (isLeftExp) {
            temp = expLeft + index + "";
            expLeft = temp;
        } else {
            temp = expRight + index + "";
            expRight = temp;
        }

        Log.d("temp", temp);
        editText.setText(temp);
    }

    private void handleEqual() {
        if (expRight == "") return;

        float result = 0;

        switch (symbol) {
            case plus:
                result = Float.parseFloat(expLeft) + Float.parseFloat(expRight);
                break;
            case minus:
                result = Float.parseFloat(expLeft) - Float.parseFloat(expRight);
                break;
            case multi:
                result = Float.parseFloat(expLeft) * Float.parseFloat(expRight);
                break;
            case divider:
                // prevent expRight equals zero
                float right = Float.parseFloat(expRight);
                if (right == 0) {
                    result = Float.parseFloat(expLeft);
                } else {
                    result = Float.parseFloat(expLeft) / right;
                }
                break;
            default:
                return;
        }

        // if both int, cast int, or like 44 == 44.0 condition.
        boolean isBothInt = !expLeft.contains(".") && !expRight.contains(".") && !(symbol == Symbols.divider);
        boolean isNeedCastInt = (result == ((int) result)) || isBothInt;

        isLeftExp = false;
        expLeft = isNeedCastInt ? ((int) result) + "" : result + "";
        expRight = "";
        editText.setText(expLeft);
    }

    private void initButton() {
        int[] ids = concat(numButtonIds, symbolButtonIds);
        int index = 0;
        for (int id : ids) {
            findViewById(ids[index]).setOnClickListener(this);
            index++;
        }
    }

    private static <T> int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}