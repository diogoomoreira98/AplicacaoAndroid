package com.example.myapplication.Inicio.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_reservas;
import com.example.myapplication.Inicio.models.CentroModel;
import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Reserva extends Fragment {

    EditText date_in;
    EditText time_in, editParticipantes,e_centro, titulo;
    EditText time_in2;
    Button btn_procurar;
    AutoCompleteTextView autoCompleteTextView;
    int idcentro;
    Cursor cursor;
    List<CentroModel> listacentros= new ArrayList<>();
  //  List<CentroModel> centros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

        date_in = view.findViewById(R.id.editDate);
        time_in = view.findViewById(R.id.editTime);
        editParticipantes = view.findViewById(R.id.editParticipantes);
        e_centro = view.findViewById(R.id.e_centro);
        titulo = view.findViewById(R.id.editTitulo);
        time_in2 = view.findViewById(R.id.editTime2);
        btn_procurar = view.findViewById(R.id.verifica_disp);
        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);
        time_in2.setInputType(InputType.TYPE_NULL);



        //Selecionar centro
        autoCompleteTextView = view.findViewById(R.id.e_centro);


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(context, Request.Method.POST, "/centros/list", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            JSONArray data = response.getJSONArray("data");


                            for (int i=0; i < data.length(); i++) {
                                String nome = data.getJSONObject(i).getString("Nome");
                                int idcentro = Integer.parseInt(data.getJSONObject(i).getString("IDCentro"));

                                listacentros.add(new CentroModel(idcentro, nome));
                            }

                                ArrayAdapter<CentroModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listacentros);
                                autoCompleteTextView.setAdapter(adapter);

                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, VolleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CentroModel centroModel = listacentros.get(position);
                //String result =  centroModel.getidcentro();
                idcentro = centroModel.getidcentro();
                Toast.makeText(context, idcentro+"", Toast.LENGTH_LONG).show();
                //autoCompleteTextView.setText(result);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //fim selecionar centro

        
        Calendar calendar= Calendar.getInstance();
        Calendar time = Calendar.getInstance();


        btn_procurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> request = new HashMap<String, String>();

                try {
                    request.put("NParticipantes", editParticipantes.getText().toString());
                    request.put("HoraInicio", time_in.getText().toString());
                    request.put("HoraFim", time_in2.getText().toString());
                    request.put("Titulo", titulo.getText().toString());
                    request.put("IDCentro", idcentro+"");

                   // CentroModel centros = e_centro.getText();

                }catch (Exception e){
                    System.out.println(e);
                }

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(context, Request.Method.POST, "/reservas/validareserva", request,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Bundle bundle = new Bundle();
                                bundle.putString("NParticipantes", editParticipantes.getText().toString());
                                bundle.putString("HoraInicio", time_in.getText().toString());
                                bundle.putString("HoraFim", time_in2.getText().toString());
                                bundle.putString("Titulo", titulo.getText().toString());
                                bundle.putString("IDCentro", idcentro+"");

                                VerificarDisp fragInfo = new VerificarDisp();
                                fragInfo.setArguments(bundle);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragmentContainerView, fragInfo);
                                transaction.commit();
                                //Está mal, falta mudar de fragmento e passar os params
                               // getParentFragmentManager().setFragmentResult("VericarDisp",bundle);



                                System.out.println(response);
                                Toast.makeText(context, response.toString()+"", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, VolleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }


        });


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