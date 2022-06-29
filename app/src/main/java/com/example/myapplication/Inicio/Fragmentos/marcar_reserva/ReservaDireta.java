package com.example.myapplication.Inicio.Fragmentos.marcar_reserva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
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
import com.example.myapplication.Inicio.Fragmentos.Reserva_direta;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Inicio.ScannerActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReservaDireta extends AppCompatActivity {

    EditText date_in;
    EditText time_in, editParticipantes, titulo;
    EditText time_in2;
    Button btn_procurar, btn_confirmarr;
    String IDSala;
    private  androidx.appcompat.widget.Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_direta);

        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IDSala = extras.getString("IDSala");
        }

        date_in = findViewById(R.id.editDate);
        time_in = findViewById(R.id.editTime);
        editParticipantes = findViewById(R.id.editParticipantes);
        btn_confirmarr = findViewById(R.id.btn_confirmarr);


        titulo = findViewById(R.id.editTitulo);
        time_in2 = findViewById(R.id.editTime2);
        btn_procurar = findViewById(R.id.verifica_disp);
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
                new DatePickerDialog(ReservaDireta.this,date,calendar.get(calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ReservaDireta.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });

        time_in2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ReservaDireta.this,timeSetListener2,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });

        btn_confirmarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar_reserva();
            }
        });
    }

    public void confirmar_reserva(){
        Map<String, String> request = new HashMap<String, String>();

        String dataformatada = "20"+date_in.getText().toString().split("/")[2]+"-"+date_in.getText().toString().split("/")[0]+"-"+date_in.getText().toString().split("/")[1];
        String horainicio = dataformatada+"T"+time_in.getText()+":00.000Z";
        String horafim = dataformatada+"T"+time_in2.getText()+":00.000Z";

        request.put("IDSala", IDSala);
        request.put("Titulo", titulo.getText()+"");
        request.put("NParticipantes", editParticipantes.getText()+"");
        request.put("HoraInicio", horainicio);
        request.put("HoraFim", horafim);

        Context context = ReservaDireta.this;
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
                            Toast.makeText(ReservaDireta.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, Inicio.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, VolleyErrorHelper.getMessage(error, ReservaDireta.this), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}