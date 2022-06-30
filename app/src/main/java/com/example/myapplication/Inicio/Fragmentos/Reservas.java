package com.example.myapplication.Inicio.Fragmentos;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_reservas;
import com.example.myapplication.Inicio.Inicio;
import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.Inicio.models.SalaModel;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;
import com.example.myapplication.Utils.notifications.ReminderBroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class Reservas extends Fragment implements listar_reservas.onRvListener{
    int posicao = 0;
     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
     listar_reservas adapterReservas;
     ArrayAdapter salasAdapter;
     Cursor cursor;
     ArrayList<ReservasModel> reservas;
    ArrayList<SalaModel> options;
     AlertDialog.Builder dialogBuilder;
     AlertDialog dialog;
     TextView editDate,editTime,editTime2,editTitulo,editParticipantes;
    AutoCompleteTextView editSala;
     String status;
     long tempo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservas, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvReservas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Context context = getActivity();

        Map<String, String> request = new HashMap<String, String>();

        try {
            SaveDataDbHelper dbHelper = new SaveDataDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {SaveDataContract.SaveData.ID_USER, SaveDataContract.SaveData.TOKEN,};
            cursor = db.query(SaveDataContract.SaveData.TABLE_NAME, projection, null, null, null, null, null);
            while(cursor.moveToNext()) {
                request.put("IDUtilizador",cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER))+"");
                request.put("token",cursor.getString(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.TOKEN)));
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/reservas/listbyuser",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length()<1){
                                //Toast.makeText(getContext(), "Não há reservas", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            reservas = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length();i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                int reserva = object.getInt("IDReserva");
                                String sala = object.getString("NomeSala").trim();
                                String titulo = object.getString("Titulo").trim();
                                String data = object.getString("HoraInicio").split("T")[0].trim();

                                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                //isoFormat.setTimeZone(TimeZone.getTimeZone("UTC+0"));
                                DecimalFormat formatter = new DecimalFormat("00");

                                Date horaInicio = isoFormat.parse(object.getString("HoraInicio"));
                                String StringhoraInicio = formatter.format(horaInicio.getHours()) +":"+ formatter.format(horaInicio.getMinutes());

                                Date horaFim = isoFormat.parse(object.getString("HoraFim"));
                                String StringhoraFim = formatter.format(horaFim.getHours()) +":"+ formatter.format(horaFim.getMinutes());

                                String centro = object.getString("NomeCentro").trim();
                                int participantes = object.getInt("NParticipantes");


                                Date datainicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(object.getString("HoraInicio"));
                                Date datafim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(object.getString("HoraFim"));
                                Date currentTime = Calendar.getInstance().getTime();


                                if(datainicio.getTime() >= currentTime.getTime()){
                                    String notifyTitulo = "Notificação de reserva";
                                    String notifyDescricao = "A sua reserva "+ titulo
                                            +"\n Começa dentro de 10 minutos."
                                            +"\n Com hora prevista final para as "+ datafim.getHours()+":"+datafim.getMinutes();

                                    ReminderBroadcast.cancelNotification(context, reserva);
                                    ReminderBroadcast.scheduleNotification(context, reserva,datainicio.getTime()-600000, notifyTitulo, notifyDescricao); //10min antes da reserva
                                }

                                String idsala = object.getString("IDSala").trim();
                                if(datainicio.getTime() <= currentTime.getTime() && datafim.getTime() >= currentTime.getTime()){
                                    status = "A decorrer";
                                    reservas.add(new ReservasModel(
                                                    reserva,
                                                    titulo,
                                                    data,
                                                    idsala,
                                                    sala,
                                                    StringhoraInicio,
                                                    StringhoraFim,
                                                    centro,
                                                    participantes,
                                                    status
                                            )
                                    );
                                }
                                else if(datainicio.getTime() >= currentTime.getTime())
                                {
                                    status = "Em breve";
                                    reservas.add(new ReservasModel(
                                                    reserva,
                                                    titulo,
                                                    data,
                                                    idsala,
                                                    sala,
                                                    StringhoraInicio,
                                                    StringhoraFim,
                                                    centro,
                                                    participantes,
                                                    status
                                            )
                                    );
                                }


                                }

                            adapterReservas= new listar_reservas(getContext(),reservas,Reservas.this::onRvClick);
                            recyclerView.setAdapter(adapterReservas);

                        } catch (JSONException | ParseException e) {
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


        return view;
    }

    @Override
    public void onRvClick(int position) {
        if(reservas.get(position).getStatus() == "A decorrer"){
            createNewDialogAumentar(position);
        }else
        {
            createNewDialog(position);
        }

        final ReservasModel idreserva = reservas.get(position);
       // Toast.makeText(getContext(),""+idreserva.getidreserva() , Toast.LENGTH_LONG).show();
    }

    public void editar_reserva(int position){

        Map<String, String> request = new HashMap<String, String>();
        final int idreserva = reservas.get(position).getidreserva();
        final int idsala = options.get(posicao).getIdsala();
        Context context = getActivity();

        String dataformatada = editDate.getText().toString();
        DecimalFormat formatter = new DecimalFormat("00");

        String iniciohora = formatter.format(Integer.parseInt(editTime.getText().toString().split(":")[0])-1);
        String iniciominutos = editTime.getText().toString().split(":")[1];
        String horainicio = dataformatada+"T"+iniciohora+":"+iniciominutos+":00.000Z";

        String fimhora = formatter.format(Integer.parseInt(editTime2.getText().toString().split(":")[0])-1);
        String fimminutos = editTime2.getText().toString().split(":")[1];
        String horafim = dataformatada+"T"+fimhora+":"+fimminutos+":00.000Z";

        /*
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String hora_fimAsISO = df.format(hora_fim);
        */

        request.put("IDReserva", idreserva+"");
        request.put("IDSala", idsala+"");
        request.put("Titulo", editTitulo.getText().toString());
        request.put("NParticipantes", editParticipantes.getText().toString());
        request.put("HoraInicio", horainicio);
        request.put("HoraFim", horafim);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.PUT,
                "/reservas/edit",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();

                            Reservas InicioFragment = new Reservas();
                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,InicioFragment).commit();
                            dialog.cancel();
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
    public void aumentar_tempo(int position, long tempo) {

            Map<String, String> request = new HashMap<String, String>();
            final int idreserva = reservas.get(position).getidreserva();
            final ReservasModel data = reservas.get(position);
            Context context = getActivity();

            request.put("IDReserva", idreserva+"");

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                    context,
                    Request.Method.POST,
                    "/reservas/listbyreserva",
                    request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Map<String, String> request = new HashMap<String, String>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                String sala="", horainicio = "", horafim = "";

                                for (int i = 0; i < jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    sala = object.getString("IDSala").trim();
                                    horainicio = object.getString("HoraInicio");
                                    horafim = object.getString("HoraFim");
                                }

                                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                //isoFormat.setTimeZone(TimeZone.getTimeZone("UTC+0"));
                                DecimalFormat formatter = new DecimalFormat("00");

                                Date horaInicio = isoFormat.parse(horainicio);
                                Date horaFim = isoFormat.parse(horafim);
                                Date horaFimMaisTempo = new Date(horaFim.getTime() + tempo * 60000);

                                String horainiciaenviar = isoFormat.format(horaInicio);
                                String horafimenviar = isoFormat.format(horaFimMaisTempo);


                                request.put("IDReserva", idreserva+"");
                                request.put("HoraInicio",horainiciaenviar);
                                request.put("HoraFim",horafimenviar);
                                request.put("IDSala",sala);

                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                                        context,
                                        Request.Method.PUT,
                                        "/reservas/prolongar",
                                        request,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();

                                                    // Reload current fragment
                                                    Reservas fragInfo = new Reservas();
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.fragmentContainerView, fragInfo);
                                                    transaction.commit();

                                                    // Reservas InicioFragment = new Reservas();
                                                    // getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,InicioFragment).commit();
                                                    //dialog.cancel();
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




                            } catch (JSONException | ParseException e) {
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

    public void inativar_reserva(int position) {
        //IDReserva, IDSala, IDCentro, IDUtilizador, Titulo, NParticipantes, HoraInicio, HoraFim, Active

        Map<String, String> request = new HashMap<String, String>();
        final int idreserva = reservas.get(position).getidreserva();
        final ReservasModel data = reservas.get(position);
        Context context = getActivity();

        request.put("IDReserva", idreserva+"");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.PUT,
                "/reservas/cancel",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                        } catch (Exception e) {
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

    public void createNewDialogAumentar(int position) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View detalhePopup2 = getLayoutInflater().inflate(R.layout.aumentar_popup,null);
        dialogBuilder.setView(detalhePopup2);
        dialog = dialogBuilder.create();
        dialog.show();
        Button btn_cancel = (Button) detalhePopup2.findViewById(R.id.btn_cancel);
        Button btn_confirm = (Button) detalhePopup2.findViewById(R.id.btn_confirm);

        RadioGroup radioGroup = (RadioGroup) detalhePopup2.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton) {
                    tempo = 10;

                } else if(checkedId == R.id.radioButton2) {
                    tempo = 15;

                } else if(checkedId == R.id.radioButton3){
                    tempo = 20;

                } else if(checkedId == R.id.radioButton4){
                    tempo = 25;

                } else if(checkedId == R.id.radioButton5){
                    tempo = 30;

                }else{
                    tempo = 0;
                    Toast.makeText(getActivity().getApplicationContext(), "Por favor escolha uma duração",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aumentar_tempo(position, tempo);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }


    public void createNewDialog(int position){

        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View detalhePopup = getLayoutInflater().inflate(R.layout.detalhepopup,null);
        dialogBuilder.setView(detalhePopup);
        dialog = dialogBuilder.create();
        dialog.show();

        editDate = (TextView) detalhePopup.findViewById(R.id.editDate);
        editTime = (TextView) detalhePopup.findViewById(R.id.editTime);
        editTime2 = (TextView) detalhePopup.findViewById(R.id.editTime2);
        editTitulo = (TextView) detalhePopup.findViewById(R.id.editTitulo);
        editParticipantes= (TextView) detalhePopup.findViewById(R.id.editParticipantes);
        editSala = (AutoCompleteTextView) detalhePopup.findViewById(R.id.drop_sala);
        Button btn_editar = (Button) detalhePopup.findViewById(R.id.btn_edit);
        Button btn_inativar = (Button) detalhePopup.findViewById(R.id.btn_ina);

        final ReservasModel data = reservas.get(position);

        editDate.setText(""+data.getData());
        editTime.setText(data.getHoraInicio().toString().split(":")[0] + ":" + data.getHoraInicio().toString().split(":")[1]);
        editTime2.setText(data.getHoraFim().toString().split(":")[0]+ ":" + data.getHoraFim().toString().split(":")[1]);
        editTitulo.setText(""+data.getTitulo());
        editParticipantes.setText(""+data.getParticipantes());


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


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),date,calendar.get(calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });

        editTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(),timeSetListener2,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        });


        btn_inativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para inativar
                inativar_reserva(position);

            }
        });



        Map<String, String> request = new HashMap<String, String>();
        Context context = getActivity();
        request.put("IDSala", data.getIdSala());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/salas/listbysala",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Map<String, String> request = new HashMap<String, String>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            String sala="", horainicio = "", horafim = "";

                            for (int i = 0; i < jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                request.put("IDCentro", object.getString("IDCentro").trim());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                                context,
                                Request.Method.POST,
                                "/salas/listbycentro",
                                request,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            options = new ArrayList<>();
                                            JSONArray jsonArray = response.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject object = jsonArray.getJSONObject(i);

                                                int idsala = object.getInt("IDSala");
                                                String nomesala = object.getString("Nome");
                                                String duracaolimpeza = object.getString("DuracaoLimpeza");
                                                String nmaxutentes = object.getString("NMaxUtentes");
                                                String alocacaomaxima = object.getString("AlocacaoMaxima");

                                                if(idsala==Integer.parseInt(data.getIdSala())){
                                                    posicao = i;
                                                }
                                                //uma possivel solução, percorrer o array até encontrar o nome da sala selecionada, ir á posição anterior buscar o valor do id
                                                options.add(new SalaModel(
                                                                idsala,
                                                                nomesala,
                                                                duracaolimpeza,
                                                                nmaxutentes,
                                                                alocacaomaxima
                                                        )
                                                );
                                            }


                                            salasAdapter = new ArrayAdapter(getContext(), R.layout.salas_item, options);
                                            editSala.setText(options.get(posicao).toString());
                                            editSala.setAdapter(salasAdapter);
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, VolleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        editSala.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                posicao = position;
            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Botão para editar a reserva
                editar_reserva(position);
            }
        });

    }

    private void updateTime(Calendar calendar2) {
        String Format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

        editTime.setText(sdf.format(calendar2.getTime()));
    }

    private void updateTime2(Calendar calendar3) {
        String Format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format,Locale.US);

        editTime2.setText(sdf.format(calendar3.getTime()));
    }

    private void updateCalendar(Calendar calendar1) {
        String Format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(Format,Locale.US);

        editDate.setText(sdf.format(calendar1.getTime()));
    }
}