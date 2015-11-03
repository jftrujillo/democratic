package com.kcumendigital.democratic.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateForumFragment extends Fragment implements View.OnClickListener {

    OnButton onButton;
    Button btn;
    RecipientEditTextView recipientEditTextView;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    TextInputLayout descripcion;

    public CreateForumFragment() {}


    @Override
    public void onClick(View v) {
        String prueba = recipientEditTextView.getText().toString();
        Log.i("dsaf",prueba);


        Toast.makeText(getActivity(),"entro",Toast.LENGTH_SHORT).show();
        onButton.OnButtonClick();

    }

    public interface OnButton{
         void OnButtonClick ();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onButton = (OnButton) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_create_forum, container, false);

        spinner = (Spinner) v.findViewById(R.id.categorias);
        descripcion = (TextInputLayout) v.findViewById(R.id.descripcion_foro);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        btn = (Button) v.findViewById(R.id.crear_foro);
        btn.setOnClickListener(this);

        recipientEditTextView = (RecipientEditTextView) v.findViewById(R.id.recipient);
        recipientEditTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        recipientEditTextView.setAdapter(new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, getActivity(), 6));



        return v;
    }

    public Discussion getNewDisucion (){
        Discussion discussion = new Discussion();
        discussion.setCategory(spinner.getSelectedItem().toString());
        discussion.setDescription(descripcion.getEditText().getText().toString());
        String tags = null;
        for (int i = 0; i<recipientEditTextView.getAdapter().getCount();i++){
            tags = tags + recipientEditTextView.getAdapter().getItem(i).toString();
            if (i == recipientEditTextView.getAdapter().getCount() - 1){
                Toast.makeText(getActivity(),"No agrega la coma en "+recipientEditTextView.getAdapter().getCount(),Toast.LENGTH_SHORT).show();
            }

            else {
                tags = tags+",";
            }
        }
        discussion.setTags(tags);
        return discussion;
    }



}
