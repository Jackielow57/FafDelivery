package com.example.user.map;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TopSection extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topsection,container,false);
        Button profileButton=(Button) view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), Profile.class);
                    startActivity(intent);
                    getActivity().finish();

            }
        });
        Button itemButton=(Button) view.findViewById(R.id.itemButton);
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ItemActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        Button trackButton=(Button) view.findViewById(R.id.trackButton);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),TraceActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Button reportButton=(Button) view.findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ReportActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}
