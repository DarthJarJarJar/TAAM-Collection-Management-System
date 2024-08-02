package com.example.b07demosummer2024;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportFragment extends Fragment {

    private Spinner detailSpinner;
    private EditText editTextReportParameter;
    private TextView detailTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);

        Spinner reportTypeSpinner = view.findViewById(R.id.reportType);
        detailSpinner = view.findViewById(R.id.detailSpinner);
        editTextReportParameter = view.findViewById(R.id.editTextReportParamter);
        detailTextView = view.findViewById(R.id.detailTextView);


        ArrayAdapter<CharSequence> reportSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.report_options, R.layout.spinner_item_right_aligned);
        reportSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypeSpinner.setAdapter(reportSpinnerAdapter);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                updateDetailSpinnerBasedOnSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        Button generateButton = view.findViewById(R.id.generateReportButton);
        generateButton.setOnClickListener(v -> makePdf());


        return view;
    }

    private void makePdf() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();



    }

    private void updateDetailSpinnerBasedOnSelection(String selectedItem) {
        detailSpinner.setVisibility(View.GONE);
        editTextReportParameter.setVisibility(View.GONE);
        detailTextView.setVisibility(View.GONE);

        switch (selectedItem) {
            case "Lot Number":
                editTextReportParameter.setHint("Enter Lot Number");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Name":
                editTextReportParameter.setHint("Enter Name");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Category":
                ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.categories_array, R.layout.spinner_item_right_aligned);
                categoryAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(categoryAdapter);
                detailTextView.setText(R.string.select_category);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "Period":
                ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(requireContext(),
                        R.array.period_array, R.layout.spinner_item_right_aligned);
                periodAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(periodAdapter);
                detailTextView.setText(R.string.select_period);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "All Items":
                break;
            default:
                break;
        }
    }
}