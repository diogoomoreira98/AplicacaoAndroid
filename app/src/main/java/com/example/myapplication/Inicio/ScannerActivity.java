package com.example.myapplication.Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.myapplication.Inicio.Fragmentos.Reserva_direta;
import com.example.myapplication.Inicio.Fragmentos.Reservas;
import com.example.myapplication.Inicio.Fragmentos.marcar_reserva.ReservaDireta;
import com.example.myapplication.R;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        Intent intent = new Intent(ScannerActivity.this, ReservaDireta.class);
        //startActivity(intent);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("IDSala", result.getText());
                        startActivity(intent);
                        this.finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}