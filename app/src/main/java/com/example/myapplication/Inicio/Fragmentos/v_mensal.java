package com.example.myapplication.Inicio.Fragmentos;

import static com.example.myapplication.Inicio.CalendarClasses.CalendarUtils.daysInMonthArray;
import static com.example.myapplication.Inicio.CalendarClasses.CalendarUtils.daysInWeekArray;
import static com.example.myapplication.Inicio.CalendarClasses.CalendarUtils.monthYearFromDate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Inicio.Adapters.listar_por_dia;
import com.example.myapplication.Inicio.CalendarClasses.CalendarAdapter;
import com.example.myapplication.Inicio.CalendarClasses.CalendarUtils;
import com.example.myapplication.Inicio.Fragmentos.marcar_reserva.EscolherCentro;
import com.example.myapplication.Inicio.models.ReservasModel;
import com.example.myapplication.Login.SaveDataContract;
import com.example.myapplication.Login.SaveDataDbHelper;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VolleyAPI.CustomJsonRequest;
import com.example.myapplication.Utils.VolleyAPI.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class v_mensal extends Fragment implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView eventListView;
    Button next,back,novaReserva;
    LinearLayoutManager layoutManager;
    Cursor cursor;
    ArrayList<ReservasModel> reservas;
    String status= "Active";
    listar_por_dia adapterReservas;
    String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_v_mensal, container, false);
        CalendarUtils.selectedDate = LocalDate.now();
        initWidgets(view);
        setWeekView();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
        novaReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ao clicar no botão nova reserva mudar para outro fragmento:
                Fragment someFragment = new EscolherCentro();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, someFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        return view;
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventListView = view.findViewById(R.id.rv_listarpordia);
        next = (Button) view.findViewById(R.id.seguinte1);
        back = (Button) view.findViewById(R.id.anterior1);
        novaReserva = (Button) view.findViewById(R.id.reservar);

        eventListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventListView.setLayoutManager(layoutManager);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        selectedDate = CalendarUtils.selectedDate+"";
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }
    private void setEventAdpater()
    {
        listar_reservas_dia();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    public void listar_reservas_dia(){

        Context context = getActivity();

        Map<String, String> request = new HashMap<String, String>();

        try {
            SaveDataDbHelper dbHelper = new SaveDataDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {SaveDataContract.SaveData.ID_USER, SaveDataContract.SaveData.TOKEN,};
            cursor = db.query(SaveDataContract.SaveData.TABLE_NAME, projection, null, null, null, null, null);
            while(cursor.moveToNext()) {
                request.put("IDUtilizador",cursor.getInt(cursor.getColumnIndexOrThrow(SaveDataContract.SaveData.ID_USER))+"");

            }
            request.put("Data",selectedDate);

            cursor.close();
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(
                context,
                Request.Method.POST,
                "/reservas/listbydia",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      //  Toast.makeText(getContext(), response+"", Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length()<1){
                                reservas = new ArrayList<>();
                                Toast.makeText(getContext(), "Não há reservas", Toast.LENGTH_SHORT).show();
                            }else{
                                reservas = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length();i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int reserva = object.getInt("IDReserva");
                                    String sala = object.getString("NomeSala").trim();
                                    String idsala = object.getString("IDSala").trim();
                                    String titulo = object.getString("Titulo").trim();
                                    String data = object.getString("HoraInicio").split("T")[0].trim();
                                    String horaInicio = object.getString("HoraInicio").split("T")[1].trim();
                                    String horaFim = object.getString("HoraFim").split("T")[1].trim();
                                    String centro = object.getString("NomeCentro").trim();
                                    int participantes = object.getInt("NParticipantes");

                                    Date datainicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(object.getString("HoraInicio"));
                                    Date datafim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(object.getString("HoraFim"));

                                        reservas.add(new ReservasModel(
                                                        reserva,
                                                        titulo,
                                                        data,
                                                        idsala,
                                                        sala,
                                                        horaInicio,
                                                        horaFim,
                                                        centro,
                                                        participantes,
                                                        status
                                                )
                                        );

                                }
                            }
                            adapterReservas= new listar_por_dia(getContext(),reservas,v_mensal.this::onRvClick);
                            eventListView.setAdapter(adapterReservas);

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

    private void onRvClick(int position) {
    }

}