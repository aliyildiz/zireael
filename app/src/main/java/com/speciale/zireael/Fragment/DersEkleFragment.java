package com.speciale.zireael.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.speciale.zireael.Model.Event;
import com.speciale.zireael.R;


import java.util.Calendar;

public class DersEkleFragment extends Fragment {


    String day;
    TextView clockTextStart, clockTextEnd;
    TextInputEditText eventText;
    ImageButton saveImage;
    int hourStart, minuteStart;
    int hourEnd, minuteEnd;
    String str1, str2, startTime, str3, str4, endTime;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Ders Ekle");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ders_ekle_fragment, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println("user:" + firebaseUser.getUid());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("zireael_DB" + "/" + firebaseUser.getUid());


        Spinner spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //spinner sonucunu string değişkenine ata veritabanına onu kaydedicez.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                day = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        eventText = view.findViewById(R.id.eventText);
        clockTextStart = view.findViewById(R.id.startText);
        clockTextEnd = view.findViewById(R.id.endText);

        Calendar mStartCurrentTime = Calendar.getInstance();//
        int startHour = mStartCurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
        int startMinute = mStartCurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
        clockTextStart.setText(String.format("%02d:%02d", startHour, startMinute));
        clockTextEnd.setText(String.format("%02d:%02d", startHour + 1, startMinute));

        str1 = Integer.toString(startHour);
        if (startHour < 10) {
            str1 = "0" + str1;
        }
        str2 = Integer.toString(startMinute);
        if ((startMinute % 100) < 10) {
            str2 = "0" + str2;
        }
        startTime = str1 + str2;

        str3 = Integer.toString(startHour + 1);
        if (startHour + 1 < 10) {
            str3 = "0" + str3;
        }
        str4 = Integer.toString(startMinute);
        if ((startMinute % 100) < 10) {
            str4 = "0" + str4;
        }
        endTime = str3 + str4;

        clockTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık

                TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

                //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                                  int selectedMinute) {

                                clockTextStart.setText(String.format("%02d:%02d", selectedHour,
                                        selectedMinute));

                                //saatTextView.setText( selectedHour + ":" + selectedMinute);
                                // Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                hourStart = selectedHour;
                                minuteStart = selectedMinute;
                                str1 = Integer.toString(hourStart);
                                if (hourStart < 10) {
                                    str1 = "0" + str1;
                                }
                                str2 = Integer.toString(minuteStart);
                                if ((minuteStart % 100) < 10) {
                                    str2 = "0" + str2;
                                    System.out.println("burada 0dan kucuk mu?    " + str2);
                                }
                                //System.out.println(hourStart + ";" + minuteStart);
                                //str1 = Integer.toString(hourStart);
                                //str2 = Integer.toString(minuteStart);
                                startTime = str1 + str2;
                                System.out.println("baslangic" + startTime);
                            }
                        }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Dersin Başlangıç Saatini Seçin");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

                timePicker.show();
            }
        });


        clockTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık

                TimePickerDialog timePicker2;

                timePicker2 = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                                  int selectedMinute) {

                                clockTextEnd.setText(String.format("%02d:%02d", selectedHour,
                                        selectedMinute));


                                //saatTextView2.setText( selectedHour + ":" + selectedMinute);
                                hourEnd = selectedHour;
                                minuteEnd = selectedMinute;
                                //System.out.println(hourEnd + ";" + minuteEnd);
                                str3 = Integer.toString(hourEnd);
                                if (hourEnd < 10) {
                                    str3 = "0" + str3;
                                }
                                str4 = Integer.toString(minuteEnd);
                                if ((minuteEnd % 100) < 10) {
                                    str4 = "0" + str4;
                                    System.out.println("burada 0dan kucuk mu?    " + str4);
                                }
                                endTime = str3 + str4;
                                //System.out.println(endTime);

                            }
                        }, hour, minute, true);
                timePicker2.setTitle("Dersin Bitiş Saatini Seçin");
                timePicker2.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker2);
                timePicker2.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker2);

                timePicker2.show();
            }
        });


        saveImage = view.findViewById(R.id.save);

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveEvent();

            }
        });


        return view;
    }


    private void saveEvent() {

        String eventName = eventText.getText().toString();
        String eventID = databaseReference.push().getKey();

        Event class1 = new Event(eventID, eventName, day, startTime, endTime);

        databaseReference.child(eventID)
                .setValue(class1);

        Fragment fragment = new DerslerFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.dersEkleFragment, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }


}

