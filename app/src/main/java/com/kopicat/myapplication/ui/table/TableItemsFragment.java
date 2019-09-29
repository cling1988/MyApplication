package com.kopicat.myapplication.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kopicat.myapplication.R;
import com.kopicat.myapplication.entity.Product;
import com.kopicat.myapplication.entity.ProductViewModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link TableItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TableItemsFragment} factory method to
 * create an instance of this fragment.
 */
public class TableItemsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ProductViewModel viewModel;
    private TableLayout table;

    public TableItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_items, container, false);

        final TextView mBranchName = view.findViewById(R.id.branch_name);
        viewModel.getBranchName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBranchName.setText(s);
            }
        });

        Button mSendBtn = view.findViewById(R.id.send_btn);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = downloadUrl(viewModel.getBranchName().getValue());
                Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
            }
        });


        table = view.findViewById(R.id.table_layout);
        Log.i("Check table is nill",""+table);
        TableRow rowHeader = new TableRow(getContext());
        rowHeader.setGravity(Gravity.CENTER);
        rowHeader.setMinimumHeight(56);
        rowHeader.setBackgroundColor(Color.GRAY);

        table.addView(rowHeader, -1);


        rowHeader.addView(createTextViewHeader(getString(R.string.label_id)));
        rowHeader.addView(createTextViewHeader(getString(R.string.label_name)));
        rowHeader.addView(createTextViewHeader(getString(R.string.label_opening)));
        rowHeader.addView(createTextViewHeader(getString(R.string.label_balance)));
        rowHeader.addView(createTextViewHeader(getString(R.string.label_total)));

        Observer<List<Product>> productListObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                loadDataIntoRow(products);
//                Toast.makeText(getContext(), "Update the list", Toast.LENGTH_SHORT).show();
            }
        };
        viewModel.getProductList().observe(getViewLifecycleOwner(), productListObserver);

        return view;
    }

    private TextView createTextViewHeader(String text) {
        TextView mHeaderText = new TextView(getContext());
        mHeaderText.setText(text);

        mHeaderText.setTypeface(null, Typeface.BOLD);
        mHeaderText.setPadding(16, 3, 16, 3);
        return mHeaderText;
    }

    private TextView createTextViewBody(String text) {
        TextView mHeaderText = new TextView(getContext());
        mHeaderText.setText(text);
        mHeaderText.setPadding(16, 3, 16, 3);

        return mHeaderText;
    }

    private void loadDataIntoRow(List<Product> products) {
        for (Product p : products) {
            table.addView(createTableRow(p), p.id);
        }
    }

    private TableRow createTableRow(Product p) {
        TableRow row = new TableRow(getContext());
        row.setGravity(Gravity.CENTER);
        row.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_black));
        row.setMinimumHeight(52);

        row.addView(createTextViewBody(p.id.toString()));
        row.addView(createTextViewBody(p.name));
        TextView open = createTextViewBody(p.opening.toString());
        open.setGravity(Gravity.RIGHT);
        row.addView(open);

        TextView balance = createTextViewBody(p.balance.toString());
        balance.setGravity(Gravity.RIGHT);
        row.addView(balance);

        TextView total = createTextViewBody(p.getTotal().toString());
        total.setGravity(Gravity.RIGHT);
        row.addView(total);

        return row;
    }

    private String downloadUrl(String text)  {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        String apiToken = "888933117:AAHQsKd0FUXU7DDi0ayrE5X1rJyYGBpY4Js";
        String chatId = "-1001353974889";
        urlString = String.format(urlString, apiToken, chatId, text);
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            URL url = new URL(urlString);

            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
//            publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);


            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
//            publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                try {
                    stream.close();
                }catch (Exception e){

                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private String readStream(InputStream stream) throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(stream);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        String response = sb.toString();

        return response;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }

}
