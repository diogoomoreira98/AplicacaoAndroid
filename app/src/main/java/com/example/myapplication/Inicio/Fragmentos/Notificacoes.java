package com.example.myapplication.Inicio.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Notificacoes extends Fragment {

    EditText date_in;
    EditText time_in, editParticipantes, titulo;
    EditText time_in2;
    Button btn_procurar, btn_confirmarr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_r_qrcode, container, false);

        date_in = view.findViewById(R.id.editDate);
        time_in = view.findViewById(R.id.editTime);
        editParticipantes = view.findViewById(R.id.editParticipantes);
        btn_confirmarr = view.findViewById(R.id.btn_confirmarr);


        titulo = view.findViewById(R.id.editTitulo);
        time_in2 = view.findViewById(R.id.editTime2);
        btn_procurar = view.findViewById(R.id.verifica_disp);
        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);
        time_in2.setInputType(InputType.TYPE_NULL);

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

        btn_confirmarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar_reserva();
            }
        });

        return view;
    }


    public void confirmar_reserva(){
        Map<String, String> request = new HashMap<String, String>();

        String dataformatada = "20"+date_in.getText().toString().split("/")[2]+"-"+date_in.getText().toString().split("/")[0]+"-"+date_in.getText().toString().split("/")[1];
        String horainicio = dataformatada+"T"+time_in.getText()+":00.000Z";
        String horafim = dataformatada+"T"+time_in2.getText()+":00.000Z";

        request.put("IDSala", "1");
        request.put("Titulo", titulo.getText()+"");
        request.put("NParticipantes", editParticipantes.getText()+"");
        request.put("HoraInicio", horainicio);
        request.put("HoraFim", horafim);

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/reservas/reservadireta",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();

                            Reservas InicioFragment = new Reservas();
                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,InicioFragment).commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, VolleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateTime(Calendar calendar2) {
        String Format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

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