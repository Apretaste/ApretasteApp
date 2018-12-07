package apretaste.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.apretaste.R;

import java.util.Calendar;

import apretaste.ui.DrawerActivity;

/**
 * Created by cjam on 20/09/18.
 */

public class InputTypeHelper {
    private String text;
    private int cantInput;
    private String[] parts;
    private EditText[] inputs;
    private Activity activity;
    private int cantFieldRequeried;
    private LinearLayout linearLayout;
    InputTypeInterface inputTypeInterface;

    public InputTypeHelper(Activity activity, LinearLayout linearLayout, String text, InputTypeInterface inputTypeInterface) {
        this.activity = activity;
        this.linearLayout = linearLayout;
        this.text = text;
        this.cantInput = getCantInput();
        this.parts = getPartsInput();
        this.inputs = getInput();
        this.cantFieldRequeried = countInputRequire();
        this.inputTypeInterface = inputTypeInterface;

    }


    public EditText[] getInput() {

        final String[] parts = text.split("\\|");
        final int cant = parts.length;

        final EditText ed[] = new EditText[cant];

        for (int i = 0; i < cant; i++) {

            String typeDates = parts[i].substring(0, 2);
            ed[i] = new EditText(this.activity);

            switch (typeDates) {

                case "t:":
                    ed[i].setHint(parts[i].substring(2));
                    break;
                case "a:":
                    ed[i].setHint(parts[i].substring(2));
                    break;

                case "u:":
                    ed[i].setFocusableInTouchMode(false);
                    ed[i].setHint(parts[i].substring(2));
                    ed[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputTypeInterface.onClickInputUpload();
                        }
                    });
                    break;

                case "p:":

                    ed[i].setHint(parts[i].substring(2));
                    ed[i].setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
                case "d:":
                    ed[i].setFocusableInTouchMode(false);
                    ed[i].setHint(parts[i].substring(2));
                    ed[i].setInputType(InputType.TYPE_CLASS_DATETIME);
                    ed[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
                    final Calendar myCalendar = Calendar.getInstance();
                    final int finalI = i;
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            inputTypeInterface.updateLabel(ed[finalI], myCalendar);
                        }

                    };
                    ed[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DatePickerDialog(activity, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    break;
                case "e:":
                    ed[i].setHint(parts[i].substring(2));
                    break;
                case "n:":
                    ed[i].setInputType(InputType.TYPE_CLASS_PHONE);
                    ed[i].setHint(parts[i].substring(2));
                    break;
                case "m:":
                    ed[i].setFocusableInTouchMode(false);
                    if (parts[i].split("\\[")[1].substring(parts[i].split("\\[")[1].length() - 1).equals("*")) {

                        ed[i].setHint(parts[i].split("\\[")[0].substring(2) + parts[i].split("\\[")[1].substring(parts[i].split("\\[")[1].length() - 1));
                    } else {

                        ed[i].setHint(parts[i].split("\\[")[0].substring(2));
                    }


                    ed[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
                    final int finalI1 = i;
                    ed[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String[] item = parts[finalI1].split("\\[")[1].split("\\]")[0].split(",");

                            AlertDialog.Builder di = new AlertDialog.Builder(activity);
                            di.setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int j = 0; j < item.length; j++) {
                                        if (which == j) {
                                            ed[finalI1].setText(item[j]);
                                            break;
                                        }
                                    }

                                }
                            });
                            di.show();
                        }

                    });

                    break;
                default:
                    ed[i].setHint(parts[i]);
                    break;
            }


        }

        return ed;


    }

    public void showInputs() {
        for (int i = 0; i < this.cantInput; i++) {
            linearLayout.addView(this.inputs[i]);
        }

    }

    public int countInputRequire() {
        int cantFieldRequeried = 0;
        for (int i = 0; i < this.inputs.length; i++) {
            if (this.parts[i].substring(this.parts[i].length() - 1).equals("*")) {
                cantFieldRequeried = cantFieldRequeried + 1;
            }

        }

        return cantFieldRequeried;
    }

    public boolean checkInputRequire(Bitmap newBitmap) {
        int fieldRequeriedValid = 0;
        /*Comprobacion de los campos que son obligatorios*/

        /*Comprobacion de los campos que son obligatorios*/
        for (int j = 0; j < this.cantFieldRequeried; j++) {

            if (newBitmap != null) {
                fieldRequeriedValid = fieldRequeriedValid + 1;
            } else {

                if (inputs[j].getText().toString().equals("")) {
                    Log.e("campo por llenar", String.valueOf(j));
                } else {
                    Log.e("campo", "campo lleno");

                    fieldRequeriedValid = fieldRequeriedValid + 1;

                }
            }
        }


        if (fieldRequeriedValid == cantFieldRequeried) {
            return true;
        }
        return false;
    }

    public int getCantInput() {
        return this.text.split("\\|").length;
    }

    public String[] getPartsInput() {
        return this.text.split("\\|");

    }

    public String getCommand() {
        String command = "";
        for (int i = 0; i < cantInput; i++) {
            String values = (inputs[i].getText().toString());
            command = command + "|" + values;
        }
        return command;
    }

    public String getTypeInput(int i) {
        return parts[i].substring(0, 2);
    }

    public interface InputTypeInterface {
        void onClickInputUpload();

        void updateLabel(EditText editText, Calendar myCalendar);
    }

    public String getParamsCallBack() {
        String params = "";
        for (int i = 0; i < cantInput; i++) {
            String values = (inputs[i].getText().toString());
            params = params + "," + "'" + values + "'";
        }

        return "[" + params.substring(1) + "]";
    }
}