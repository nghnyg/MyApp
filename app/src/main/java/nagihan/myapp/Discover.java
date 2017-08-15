package nagihan.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

import nagihan.myapp.models.MySingleton;
import nagihan.myapp.models.Person;
import nagihan.myapp.models.PersonAdapter;


public class Discover extends AppCompatActivity {

    RecyclerView rycView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);

        rycView = (RecyclerView) findViewById(R.id.rycView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rycView.setLayoutManager(manager);

        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        final ArrayList<Person>persons = new JsonConverter<Person>()
                                .toArrayList(response, Person.class);

                        PersonAdapter adapter = new PersonAdapter(getApplicationContext(), persons);

                        adapter.setmOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int itemPosition =rycView.getChildLayoutPosition(view);

                                Person person = persons.get(itemPosition);

                            }
                        });

                        rycView.setAdapter(adapter);


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}


