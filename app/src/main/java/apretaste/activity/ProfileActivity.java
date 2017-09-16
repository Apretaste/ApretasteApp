package apretaste.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apretaste.R;

import apretaste.Profile;
import apretaste.ProfileInfo;
import apretaste.email.Mailer;
import apretaste.email.Mailerlistener;
import apretaste.Helper.NetworkHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements Mailerlistener {

    private static final int REQUEST_IMAGE_GET=0;
    public static ProfileInfo profileI;
    public static final String SIZE = "Size ";
    public static final String RESP = "resp";
    public static final String PROFILE_PNG = "profile.png";
    public static final String RELIGION = "Religión";
    public static final String OTRA = "Otra";
    public static final String BUDISMO = "Budismo";
    public static final String SIJISMO = "Sijismo";
    public static final String ESPIRITISMO = "Espiritismo";
    public static final String JUDAISMO = "Judaísmo";
    public static final String RAFTAFARISMO = "Raftafarismo";
    public static final String ISLAM = "Islam";
    public static final String CRISTIANISMO = "Cristianismo";
    public static final String CATOLICISMO = "Catolicismo";
    public static final String AGNOSTICISMO = "Agnosticismo";
    public static final String SECULARISMO = "Secularismo";
    public static final String ATEÍSMO = "Ateísmo";
    public static final String PROVINCIA = "Provincia";
    public static final String ISLA_DE_LA_JUVENTUD = "Isla de la Juventud";
    public static final String GUANTÁNAMO = "Guantánamo";
    public static final String SANTIAGO = "Santiago";
    public static final String GRANMA = "Granma";
    public static final String HOLGUÍN = "Holguín";
    public static final String LAS_TUNAS = "Las Tunas";
    public static final String CAMAGÜEY = "Camagüey";
    public static final String CIEGO = "Ciego";
    public static final String SANCTI_SPÍRITUS = "Sancti Spíritus";
    public static final String CIENFUEGOS = "Cienfuegos";
    public static final String LAS_VILLAS = "Las Villas";
    public static final String MATANZAS = "Matanzas";
    public static final String MAYABEQUE = "Mayabeque";
    public static final String ARTEMISA = "Artemisa";
    public static final String LA_HABANA = "La Habana";
    public static final String PINAR_DEL_RÍO = "Pinar del Río";
    public static final String NIVEL_ESCOLAR = "Nivel Escolar";
    public static final String OTRO = "Otro";
    public static final String DOCTORADO = "Doctorado";
    public static final String POSTGRADUADO = "Postgraduado";
    public static final String UNIVERSITARIO = "Universitario";
    public static final String TÉCNICO = "Técnico";
    public static final String SECUNDARIA = "Secundaria";
    public static final String PRIMARIA = "Primaria";
    public static final String ESTADO_CIVIL = "Estado Civil";
    public static final String CASADO = "Casado";
    public static final String COMPROMETIDO = "Comprometido";
    public static final String SALIENDO = "Saliendo";
    public static final String SOLTERO = "Soltero";
    public static final String PIEL = "Piel";
    public static final String MESTIZA = "Mestiza";
    public static final String NEGRA = "Negra";
    public static final String BLANCA = "Blanca";
    public static final String PELO = "Pelo";
    public static final String BLANCO = "Blanco";
    public static final String ROJO = "Rojo";
    public static final String NEGRO = "Negro";
    public static final String RUBIO = "Rubio";
    public static final String CASTAÑO = "Castaño";
    public static final String TRIGUEÑO = "Trigueño";
    public static final String OJOS = "Ojos";
    public static final String AVELLANA = "Avellana";
    public static final String AZULES = "Azules";
    public static final String VERDES = "Verdes";
    public static final String CARMELITAS = "Carmelitas";
    public static final String NEGROS = "Negros";
    public static final String CUERPO = "Cuerpo";
    public static final String ATLÉTICO = "Atlético";
    public static final String EXTRA = "Extra";
    public static final String MEDIO = "Medio";
    public static final String DELGADO = "Delgado";
    public static final String ORIENTACION_SEXUAL = "Orientacion Sexual";
    public static final String BISEXUAL = "Bisexual";
    public static final String GAY = "Gay";
    public static final String HETERO = "Hetero";
    public static final String SEXO = "Sexo";
    public static final String FEMENINO = "Femenino";
    public static final String MASCULINO = "Masculino";
    public static final String DATE_PICKER = "datePicker";
    public static final String SE_HAN_GUARDADO_SUS_DATOS_DE_PERFIL = "Se han guardado sus datos de perfil";
    public static final String PERFIL_BULK = "PERFIL BULK ";
    public static final String FOTO = "FOTO";
    public static final String RELIGION1 = "RELIGION";
    public static final String INTERESES = "INTERESES";
    public static final String PROVINCIA1 = "PROVINCIA";
    public static final String CIUDAD = "CIUDAD";
    public static final String PROFESION = "PROFESION";
    public static final String NIVEL = "NIVEL";
    public static final String ESTADO = "ESTADO";
    public static final String PIEL1 = "PIEL";
    public static final String PELO1 = "PELO";
    public static final String OJOS1 = "OJOS";
    public static final String CUERPO1 = "CUERPO";
    public static final String CUMPLEANOS = "CUMPLEANOS";
    public static final String PHONE = "PHONE";
    public static final String ORIENTACION = "ORIENTACION";
    public static final String SEXO1 = "SEXO";
    public static final String NOMBRE = "NOMBRE";
    public static final String ESCRIBA_UN_INTERES = "Escriba un interés";
    public static final String CANCELAR = "Cancelar";
    public static final String ACEPTAR = "Aceptar";
    public static final String IMAGE = "image/*";

    ImageView profilePict;
    NetworkHelper conn = new NetworkHelper();
    Profile tempProfile;
    ProfileInfo profileInfo;
    Uri fullPhotoUri=null;
    Bitmap newBitmap=null;


    private final static int REQUEST_STORAGE_PERMISSION=1;

    boolean mIsImageHidden;

    public static final String[] sexo=new String[]{MASCULINO, FEMENINO};
    public static final String[] sexoVal=new String[]{"M", "F"};
    public static final String[] orientacion=new String[]{HETERO, GAY, BISEXUAL};
    public static final String[] orientacionVal=new String[]{"HETERO", "HOMO", "BI"};
    public static final String[] cuerpo=new String[]{DELGADO, MEDIO, EXTRA, ATLÉTICO};
    public static final String[] cuerpoVal=new String[]{"DELGADO", "MEDIO", "EXTRA", "ATLETICO"};
    public static final String[] ojos=new String[]{NEGROS, CARMELITAS, VERDES, AZULES, AVELLANA, OTRO};
    public static final String[] ojosVal=new String[]{"NEGRO", "CARMELITA", "VERDE", "AZUL", "AVELLANA", "OTRO"};
    public static final String[] pelo=new String[]{TRIGUEÑO, CASTAÑO, RUBIO, NEGRO, ROJO, BLANCO, OTRO};
    public static final String[] peloVal=new String[]{"TRIGUENO", "CASTANO", "RUBIO", "NEGRO", "ROJO", "BLANCO", "OTRO"};
    public static final String[] piel=new String[]{BLANCA, NEGRA, MESTIZA, OTRO};
    public static final String[] pielVal=new String[]{"BLANCO", "NEGRO", "MESTIZO", "OTRO"};
    public static final String[] estadocivil=new String[]{SOLTERO, SALIENDO, COMPROMETIDO, CASADO};
    public static final String[] estadocivilVal=new String[]{"SOLTERO", "SALIENDO", "COMPROMETIDO", "CASADO"};
    public static final String[] nivelescolar=new String[]{PRIMARIA, SECUNDARIA, TÉCNICO, UNIVERSITARIO, POSTGRADUADO, DOCTORADO, OTRO};
    public static final String[] nivelescolarVal=new String[]{"PRIMARIO", "SECUNDARIO", "TEXNICO", "UNIVERSITARIO", "POSTGRADUADO", "DOCTORADO", "OTRO"};
    public static final String[] provincia=new String[]{PINAR_DEL_RÍO, LA_HABANA, ARTEMISA, MAYABEQUE, MATANZAS, LAS_VILLAS, CIENFUEGOS, SANCTI_SPÍRITUS, CIEGO, CAMAGÜEY, LAS_TUNAS, HOLGUÍN, GRANMA, SANTIAGO, GUANTÁNAMO, ISLA_DE_LA_JUVENTUD};
    public static final String[] provinciaVal=new String[]{"PINAR_DEL_RIO", "LA_HABANA", "ARTEMISA", "MAYABEQUE", "MATANZAS", "VILLAS_CLARA", "CIENFUEGOS", "SANCTI_SPIRITUS", "CIEGO", "CAMAGUEY", "LAS_TUNAS", "HOLGUIN", "GRANMA", "SANTIAGO_DE_CUBA", "GUANTANAMO", "ISLA_DE_LA_JUVENTUD"};
    public static final String[] religion=new String[]{ATEÍSMO, SECULARISMO, AGNOSTICISMO, CATOLICISMO, CRISTIANISMO, ISLAM, RAFTAFARISMO, JUDAISMO, ESPIRITISMO, SIJISMO, BUDISMO, OTRA};
    public static final String[] religionVal=new String[]{"ATEISMO", "SECULARISMO", "AGNOSTICISMO", "CATOLICISMO", "CRISTIANISMO", "ISLAM", "RAFTAFARISMO", "JUDAISMO", "ESPIRITISMO", "SIJISMO", "BUDISMO", "OTRA"};


    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private void picPict()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode==REQUEST_STORAGE_PERMISSION)
       {
                if(grantResults.length>0
                        && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                   picPict();
                }
                else
                {
                    Toast.makeText(this,"Permiso no autorizado",Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePict=((ImageView)findViewById(R.id.ib_profile_photo));



        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(profilePict, VIEW_NAME_HEADER_IMAGE);






        if(MainActivity.pro.profile.picture!=null && !MainActivity.pro.profile.picture.isEmpty())
        {
            try {
                RoundedBitmapDrawable dr= RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(new File(getFilesDir(),MainActivity.pro.profile.picture).getPath()));
                dr.setCircular(true);
                profilePict.setImageDrawable(dr);
            }
            catch (Exception e)
            {

            }
        }
        else
            profilePict.setImageResource(R.drawable.ic_person_black_24dp);

        final EditText username = (EditText) findViewById(R.id.et_username);
        final EditText name=(EditText)findViewById(R.id.et_profile_name);
        final EditText cell=(EditText)findViewById(R.id.et_profile_celular);
        final EditText profesion=(EditText)findViewById(R.id.et_profile_profesion);
        final EditText ciudad=(EditText)findViewById(R.id.et_profile_city);
        final EditText intereses=(EditText)findViewById(R.id.et_profile_intereses);
        final EditText birthday=(EditText)findViewById(R.id.et_profile_birthday);
        final EditText sex=(EditText)findViewById(R.id.et_profile_sexo);
        final EditText orientation=(EditText)findViewById(R.id.et_profile_orientsexu);
        final EditText typebody=(EditText)findViewById(R.id.et_profile_typebody);
        final EditText eyes=(EditText)findViewById(R.id.et_profile_eyes);
        final EditText hair=(EditText)findViewById(R.id.et_profile_pelo);
        final EditText skin=(EditText)findViewById(R.id.et_profile_piel);
        final EditText state=(EditText)findViewById(R.id.et_profile_state);
        final EditText scolar=(EditText)findViewById(R.id.et_profile_nivel_escolar);
        final EditText province=(EditText)findViewById(R.id.et_profile_province);
        final EditText relig=(EditText)findViewById(R.id.et_profile_religion);


        findViewById(R.id.pfl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("prof","imagepick");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int checkpermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(checkpermission!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        },REQUEST_STORAGE_PERMISSION);

                    }
                    else
                        picPict();
                }
                else
                    picPict();

            }
        });

        username.setText(MainActivity.pro.username);
        name.setText(MainActivity.pro.profile.full_name);
        cell.setText(MainActivity.pro.profile.phone);
        profesion.setText(MainActivity.pro.profile.occupation);
        ciudad.setText(MainActivity.pro.profile.city);

        tempProfile=MainActivity.pro.profile.clone();




        String interests="";
        final List<String> ints = new ArrayList<String>();
        if(MainActivity.pro.profile.interests!=null && MainActivity.pro.profile.interests.length>0)
        {
            Collections.addAll(ints, MainActivity.pro.profile.interests);
            for (String val :
                    ints) {
                interests+=val+",";
            }
            if(interests.length()>1)
            interests=interests.substring(0,interests.length()-1);
        }

        intereses.setText(interests);
        intereses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inf=getLayoutInflater().inflate(R.layout.interests_dialog,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this).setView(inf).setPositiveButton(ACEPTAR,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tempProfile.interests=ints.toArray(tempProfile.interests);
                                String interests="";
                                if(ints.size()>0)
                                    for (String val :
                                            ints) {
                                        interests += val + ",";
                                    }
                                if(interests.length()>0)
                                    interests=interests.substring(0,interests.length()-1);
                                intereses.setText(interests);
                            }
                        }).setNegativeButton(CANCELAR,null);
                final TextView tv=(TextView)inf.findViewById(R.id.interest);
                ListView lv=(ListView)inf.findViewById(R.id.listview);
                final BaseAdapter adapter=new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return ints.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View v;
                        if(convertView==null)
                        {
                            v=getLayoutInflater().inflate(R.layout.interest_item,null);
                            ((TextView)v.findViewById(R.id.entry_title)).setText(ints.get(position));
                            v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ints.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                        else
                        {
                            v=getLayoutInflater().inflate(R.layout.interest_item,null);
                            ((TextView)v.findViewById(R.id.entry_title)).setText(ints.get(position));
                            v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ints.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                        return v;
                    }
                };
                inf.findViewById(R.id.addbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt=tv.getText().toString();
                        if(txt.isEmpty())
                        {
                            Toast.makeText(ProfileActivity.this, ESCRIBA_UN_INTERES, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ints.add(txt);
                        adapter.notifyDataSetChanged();
                        tv.setText("");
                    }
                });
                lv.setAdapter(adapter);
                builder.show();
            }
        });

        sex.setText(MainActivity.pro.profile.gender);
        birthday.setText(MainActivity.pro.profile.date_of_birth);
        orientation.setText(MainActivity.pro.profile.sexual_orientation);
        typebody.setText(MainActivity.pro.profile.body_type);
        eyes.setText(MainActivity.pro.profile.eyes);
        hair.setText(MainActivity.pro.profile.hair);
        skin.setText(MainActivity.pro.profile.skin);
        state.setText(MainActivity.pro.profile.marital_status);
        scolar.setText(MainActivity.pro.profile.highest_school_level);
        province.setText(MainActivity.pro.profile.province);
        relig.setText(MainActivity.pro.profile.religion);

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> bulkInfo=new HashMap<>();
                String text;

                if(!(text=username.getText().toString()).equals(MainActivity.pro.username) && !text.isEmpty())
                {




                    if (text.length() > 15){
                        Toast.makeText(getBaseContext(),"El nombre de usuario debe tener menos de 15 carateres",Toast.LENGTH_SHORT).show();
                    }else{

                        if (conn.haveConn(ProfileActivity.this)){
                            MainActivity.pro.change_un(text);
                            bulkInfo.put("username",text);
                        }else{
                            Toast.makeText(getBaseContext(),"No hay conexion activa",Toast.LENGTH_SHORT).show();
                        }
                    }





                }

                if(!(text=name.getText().toString()).equals(MainActivity.pro.profile.full_name) && !text.isEmpty())
                {
                    bulkInfo.put(NOMBRE,text);
                    tempProfile.full_name=text;
                }

                if(!(text=sex.getText().toString()).equals(MainActivity.pro.profile.gender) && !text.isEmpty())
                {
                    bulkInfo.put(SEXO1,getParallelValue(sexo,sexoVal,text));
                    tempProfile.gender=text;
                }

                if(!(text=orientation.getText().toString()).equals(MainActivity.pro.profile.sexual_orientation) && !text.isEmpty())
                {
                    bulkInfo.put(ORIENTACION,getParallelValue(orientacion,orientacionVal,text));
                    tempProfile.sexual_orientation=text;
                }

                if(!(text=cell.getText().toString()).equals(MainActivity.pro.profile.phone) && !text.isEmpty())
                {
                    bulkInfo.put(PHONE,text);
                    tempProfile.phone =text;
                }

                if(!(text=birthday.getText().toString()).equals(MainActivity.pro.profile.date_of_birth) && !text.isEmpty())
                {
                    bulkInfo.put(CUMPLEANOS,text);
                    tempProfile.date_of_birth=text;
                }

                if(!(text=typebody.getText().toString()).equals(MainActivity.pro.profile.body_type) && !text.isEmpty())
                {
                    bulkInfo.put(CUERPO1,getParallelValue(cuerpo,cuerpoVal,text));
                    tempProfile.body_type=text;
                }

                if(!(text=eyes.getText().toString()).equals(MainActivity.pro.profile.eyes) && !text.isEmpty())
                {
                    bulkInfo.put(OJOS1,getParallelValue(ojos,ojosVal,text));
                    tempProfile.eyes=text;
                }

                if(!(text=hair.getText().toString()).equals(MainActivity.pro.profile.hair) && !text.isEmpty())
                {
                    bulkInfo.put(PELO1,getParallelValue(pelo,peloVal,text));
                    tempProfile.hair=text;
                }

                if(!(text=skin.getText().toString()).equals(MainActivity.pro.profile.skin) && !text.isEmpty())
                {
                    bulkInfo.put(PIEL1,getParallelValue(piel,pielVal,text));
                    tempProfile.skin=text;
                }

                if(!(text=state.getText().toString()).equals(MainActivity.pro.profile.marital_status) && !text.isEmpty())
                {
                    bulkInfo.put(ESTADO,getParallelValue(estadocivil,estadocivilVal,text));
                    tempProfile.marital_status=text;
                }

                if(!(text=scolar.getText().toString()).equals(MainActivity.pro.profile.highest_school_level) && !text.isEmpty())
                {
                    bulkInfo.put(NIVEL,getParallelValue(nivelescolar,nivelescolarVal,text));
                    tempProfile.highest_school_level=text;
                }

                if(!(text=profesion.getText().toString()).equals(MainActivity.pro.profile.occupation) && !text.isEmpty())
                {
                    bulkInfo.put(PROFESION,text);
                    tempProfile.occupation=text;
                }

                if(!(text=ciudad.getText().toString()).equals(MainActivity.pro.profile.city) && !text.isEmpty())
                {
                    bulkInfo.put(CIUDAD,text);
                    tempProfile.city=text;
                }

                if(!(text=province.getText().toString()).equals(MainActivity.pro.profile.province) && !text.isEmpty())
                {
                    bulkInfo.put(PROVINCIA1,getParallelValue(provincia,provinciaVal,text));
                    tempProfile.province=text;
                }

                String interests="";
                if(ints.size()>0)
                    for (String val :
                            MainActivity.pro.profile.interests) {
                        interests += val + ",";
                    }
                if(interests.length()>0)
                    interests=interests.substring(0,interests.length()-1);
                if(!(text=intereses.getText().toString()).equals(interests) && !text.isEmpty())
                {
                    bulkInfo.put(INTERESES,text);
                    tempProfile.interests=text.split(",");
                }

                if(!(text=relig.getText().toString()).equals(MainActivity.pro.profile.religion) && !text.isEmpty())
                {
                    bulkInfo.put(RELIGION1,getParallelValue(religion,religionVal,text));
                    tempProfile.religion=text;
                }


                if(newBitmap!=null)
                {
                    bulkInfo.put(FOTO, PROFILE_PNG);
                    tempProfile.picture= PROFILE_PNG;
                }

                if(bulkInfo.size()>0)
                {
                    String json=new Gson().toJson(bulkInfo);
                    Mailer mailer=new Mailer(ProfileActivity.this,null, PERFIL_BULK +json,true, SE_HAN_GUARDADO_SUS_DATOS_DE_PERFIL,ProfileActivity.this).setCustomText("Estamos guardando su perfil. Sea paciente y no cierre la aplicación.").setShowCommand(false);
                    mailer.setAttachedbitmap(newBitmap);
                    mailer.setAppendPassword(true);

                    mailer.execute();
                }
                else {
                    Toast.makeText(ProfileActivity.this, "No hay datos que guardar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bund=new Bundle();
                bund.putString(DatePickerFragment.DATE,birthday.getText().toString());
                newFragment.setArguments(bund);
                newFragment.show(getSupportFragmentManager(), DATE_PICKER);
            }
        });

        View.OnClickListener sexolListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(sexo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(sexo[which]);
                    }
                }).setTitle(SEXO).show();
            }
        };
        sex.setOnClickListener(sexolListener);

        View.OnClickListener orientacionListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(orientacion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(orientacion[which]);
                    }
                }).setTitle(ORIENTACION_SEXUAL).show();
            }
        };
        orientation.setOnClickListener(orientacionListener);


        View.OnClickListener cuerpoListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(cuerpo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(cuerpo[which]);
                    }
                }).setTitle(CUERPO).show();
            }
        };
        typebody.setOnClickListener(cuerpoListener);



        View.OnClickListener ojosListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(ojos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(ojos[which]);
                    }
                }).setTitle(OJOS).show();
            }
        };
        eyes.setOnClickListener(ojosListener);



        View.OnClickListener peloListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(pelo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(pelo[which]);
                    }
                }).setTitle(PELO).show();
            }
        };
        hair.setOnClickListener(peloListener);


        View.OnClickListener pielListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(piel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(piel[which]);
                    }
                }).setTitle(PIEL).show();
            }
        };
        skin.setOnClickListener(pielListener);


        View.OnClickListener estadocivilListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(estadocivil, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(estadocivil[which]);
                    }
                }).setTitle(ESTADO_CIVIL).show();
            }
        };
        state.setOnClickListener(estadocivilListener);



        View.OnClickListener nivelescolarListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(nivelescolar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(nivelescolar[which]);
                    }
                }).setTitle(NIVEL_ESCOLAR).show();
            }
        };
        scolar.setOnClickListener(nivelescolarListener);



        View.OnClickListener provinciaListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(provincia, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(provincia[which]);
                    }
                }).setTitle(PROVINCIA).show();
            }
        };
        province.setOnClickListener(provinciaListener);


        View.OnClickListener religionListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(ProfileActivity.this).setItems(religion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(religion[which]);
                    }
                }).setTitle(RELIGION).show();
            }
        };
        relig.setOnClickListener(religionListener);

        final CollapsingToolbarLayout ctl= (CollapsingToolbarLayout) findViewById(R.id.colltoolbar);

    }

    @Override
    public void onMailSent() {
        Log.e("pa","mail sent");
        if(newBitmap!=null)
        {
            try {
                File f=new File(getFilesDir(), PROFILE_PNG);
                if(f.exists())
                    f.delete();
                f.createNewFile();
                FileOutputStream fos=new FileOutputStream(f);
                newBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            }
            catch (Exception e)
            {

            }
        }
        MainActivity.needsReload=true;
        MainActivity.pro.profile=tempProfile;
        String pro=new Gson().toJson( MainActivity.pro);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(RESP,pro).apply();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String DATE = "date";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String date="";
            final Object o = getArguments().get(DATE);
            try {
                date= (String) o;
            } catch (ClassCastException ignored) {
            }

            final Calendar c = Calendar.getInstance();
            try {
                if(!date.isEmpty())
                c.setTime(new SimpleDateFormat(DD_MM_YYYY).parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            if(month==12)month=11;
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return itandroid.R.style.Theme_Holo_Light_Dialog_NoActionBar
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((TextView)getActivity().findViewById(R.id.et_profile_birthday)).setText(day+"/"+(month+1)+"/"+year);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.outHeight=256;
            opts.outWidth=256;
            try {
                String[] filePathColumn = { MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(fullPhotoUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();


                newBitmap=decodeSampledBitmapFromFile(path,250,250);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), newBitmap);
                roundedBitmapDrawable.setCircular(true);
                profilePict.setImageDrawable(roundedBitmapDrawable);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        options.inJustDecodeBounds = false;
        Bitmap bmp= BitmapFactory.decodeFile(path, options);

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (w > reqHeight)
        {
            float ratio = (float)w / reqHeight;
            w = reqWidth;
            h = (int) ((float) h / ratio);
        }
        if (h > reqHeight) {
            float ratio = (float)h / reqWidth;
            w = (int) ((float) w / ratio);
            h = reqHeight;
        }

        return Bitmap.createScaledBitmap(bmp,w,h,true);
    }

    public static String getParallelValue(String[] origin, String[] image, String key)
    {
        try {
            for (int i = 0; i < origin.length; i++) {
                String al = origin[i];
                if (al.equals(key))
                    return image[i];
            }
        }catch (Exception ignored)
        {}
        return key;
    }

}
