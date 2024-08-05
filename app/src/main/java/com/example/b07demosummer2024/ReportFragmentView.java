package com.example.b07demosummer2024;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportFragmentView extends Fragment {

    private ReportFragmentPresenter presenter;

    Spinner detailSpinner;
    Spinner reportTypeSpinner;
    EditText editTextReportParameter;
    TextView detailTextView;
    CheckBox descriptionAndImageOnlyCheckbox;
    Button generateButton;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        presenter = new ReportFragmentPresenter(this, new ReportFragmentModel());

        reportTypeSpinner = view.findViewById(R.id.reportType);
        detailSpinner = view.findViewById(R.id.detailSpinner);
        editTextReportParameter = view.findViewById(R.id.editTextReportParamter);
        detailTextView = view.findViewById(R.id.detailTextView);
        descriptionAndImageOnlyCheckbox = view.findViewById(R.id.descriptionAndPictureOnlyCheckBox);
        progressBar = view.findViewById(R.id.progressBarForReport);
        generateButton = view.findViewById(R.id.generateReportButton);

        ArrayAdapter<CharSequence> reportSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.report_options, R.layout.spinner_item_right_aligned);
        reportSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypeSpinner.setAdapter(reportSpinnerAdapter);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                presenter.updateDetailSpinnerBasedOnSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        generateButton.setOnClickListener(v -> presenter.makePdf());

        return view;
    }



}