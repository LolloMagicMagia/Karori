package com.example.karori.menuFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.karori.Adapter.AdapterChangeRiassunti;
import com.example.karori.R;
import com.example.karori.SearchClasses.SearchActivity;
import com.google.android.material.tabs.TabLayout;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentHome extends Fragment {
    private static final String KEY_INDEX="index";
    private ImageButton mattina;
    private ImageButton pomeriggio;
    private ImageButton sera;
    private Button riassunti;
    int positionRiassunti;
    private int where_MPS;
    String fronsblix;
    private TextView totProteineBar;
    private TextView cal_now;
    private CircularProgressIndicator progressBar;
    private String pasto = "";
    private CircleImageView bottoneChangeActivity;


    public FragmentHome() {
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fronsblix="0";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        cal_now= (TextView) view.findViewById(R.id.caloriePercentuali);

        riassunti=view.findViewById(R.id.Riassunti);
        ViewPager2 viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        bottoneChangeActivity = view.findViewById(R.id.changeActivity);

        AdapterChangeRiassunti adapter = new AdapterChangeRiassunti(this);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //PER COLORARE IL TESTO
                /* tablayout.setTabTextColors(Color.GRAY, Color.GREEN); // set the tab text colors for the both states of the tab.*/
                viewPager.setCurrentItem(tab.getPosition());
                fronsblix = Integer.toString(tab.getPosition());
                bottoneChangeActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("pasto", fronsblix);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //nothing..
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String fronsblix = Integer.toString(tab.getPosition());
                bottoneChangeActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("pasto",fronsblix);
                        startActivity(intent);
                    }
                });
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionRiassunti=position;
                tabLayout.getTabAt(position).select();
            }
        });

        riassunti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopupFragment popup = new MyPopupFragment(positionRiassunti);
                popup.show(getChildFragmentManager(), "popup");
            }
        });



        //barra inizializzazione
        progressBar=(CircularProgressIndicator) view.findViewById(R.id.circular_progress);
        //così va da 0 a 100
        progressBar.setMaxProgress(100);


        //SERVE PER CAMBIARE ACTIVITY ALLA PRESSIONE DEL BOTTONE, E POI TRAMITE IL WHERE CHE SAREBBE
        //UN VALORE PER INDICARE SE ERO SU COLAZ/PRANZ... SCELGO IL FRAGMENT DA METTERE SULLA ACTIVITY

        //tot proteine mi serve per capire a quale percentuale è arrivato , così lascio sempre settato a 100 il max
        progressBarUpdate(200, 300);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, where_MPS);
    }

    public  void progressBarUpdate(float totProteineAssunte, float totProteine){
        if(totProteineAssunte>totProteine){
            cal_now.setText(">100 %");
            progressBar.setCurrentProgress(totProteine);
        }
        else{
            cal_now.setText(String.valueOf(totProteineAssunte)+" / "+ String.valueOf(totProteine));
            progressBar.setCurrentProgress((int)((totProteineAssunte/totProteine)*100));
        }

    }
}
