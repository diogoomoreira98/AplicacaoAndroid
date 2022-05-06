package com.example.myapplication.Inicio.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.myapplication.Login.Registo;
import com.example.myapplication.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reserva extends Fragment {

    EditText date_in;
    EditText time_in;
    EditText time_in2;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

        date_in = view.findViewById(R.id.editDate);
        time_in = view.findViewById(R.id.editTime);
        time_in2 = view.findViewById(R.id.editTime2);

        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);
        time_in2.setInputType(InputType.TYPE_NULL);

        //Selecionar centro
        autoCompleteTextView = view.findViewById(R.id.e_centro);
        String[]option = {"Viseu", "Algarve", "Tondela","França"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.centros_item , option);
        autoCompleteTextView.setAdapter(arrayAdapter);
        
        Calendar calendar= Calendar.getInstance();
        Calendar time = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                updateCalendar(calendar);
            }
        };

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY,hourOfDay);
                time.set(Calendar.MINUTE,minute);
               // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                //time_in.setText(simpleDateFormat.format(calendar));
                updateTime(time);
            }
        };

        TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY,hourOfDay);
                time.set(Calendar.MINUTE,minute);
                // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                //time_in.setText(simpleDateFormat.format(calendar));
                updateTime2(time);
            }
        };


        date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),date,calendar.get(calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });

        time_in2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),timeSetListener2,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });

        return view;
    }

    private void updateTime(Calendar calendar2) {
        String Format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format,Locale.US);

        time_in.setText(sdf.format(calendar2.getTime()));
    }

    private void updateTime2(Calendar calendar3) {
        String Format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format,Locale.US);

        time_in2.setText(sdf.format(calendar3.getTime()));
    }

    private void updateCalendar(Calendar calendar1) {
        String Format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format,Locale.US);

        date_in.setText(sdf.format(calendar1.getTime()));
    }
}