package urbanhack.reportapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ImageView reportImage;
    private Report report;
    private Spinner categorySpinner;
    private EditText reportTitle;
    private ProgressDialog barProgressDialog;
    private AutoCompleteTextView locationActv;
    private HashSet<String> selectedLanguages;
    private List<String> languageList;
    private TextView languageSelectTv;
    private TextView languageTv;
    private TextView description;
    private ImageView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        TypefaceHelper.typeface(this);
        report = new Report();
        initViews();
    }

    private void initViews() {
        initReportImage();
        initIndustry();
        initLocation();
        initTags();
        initDescription();
        initFab();
    }

    private void initFab() {
        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    save();
                }catch (Exception e){
//                    Toast.makeText(getA)
                }

            }
        });
        com.github.clans.fab.FloatingActionButton fabLocal = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item);
        fabLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Saved Locally!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDescription() {
        description = (TextView) findViewById(R.id.tv_edit_profile_summary_body);
    }

    private  void save() throws Exception{
        List<String> listOfTags = new ArrayList<>();
        String category =(String)categorySpinner.getSelectedItem();

        String tit = "";
        if(reportTitle.getText()!=null && !reportTitle.getText().toString().isEmpty())
            tit = reportTitle.getText().toString();
        else {
            Toast.makeText(getApplicationContext(),"Please enter title!",Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        String desc = "";
        if(description.getText()!=null && !description.getText().toString().isEmpty())
            desc = description.getText().toString();
        else{
            Toast.makeText(getApplicationContext(),"Please enter Description!",Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        String loc = "";
        if(locationActv.getText()!=null && !locationActv.getText().toString().isEmpty())
            loc = locationActv.getText().toString();
        else {
            Toast.makeText(getApplicationContext(), "Please select a enter location!", Toast.LENGTH_LONG).show();
            throw new Exception();
        }



        if(selectedLanguages.size() == 0 ){
            Toast.makeText(getApplicationContext(),"Please select a Tag!",Toast.LENGTH_LONG).show();
            throw new Exception();
        }


        for(String tag:selectedLanguages){
            listOfTags.add(tag);
        }
        report.setCategory(category);
        report.setTags(listOfTags);
        report.setDateOfIncident(new DateTime().toDate());
        SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(this);
        String author = preferenceUtil.getData("author", "author");
        String authorUrl = preferenceUtil.getData("authorUrl","url");
        report.setAuthor(author);
        report.setAuthorImgUrl(authorUrl);
        report.setTitle(tit);
        report.setContent(desc);
        report.setAddressOfIncident(loc);
        Logger.logError("Report is "+report);

        if(report.getLattitude() == 0.0 || report.getLongitude() == 0.0){
            Toast.makeText(getApplicationContext(),"Location details couldn't not be loaded! Try Again....",Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        saveReport("http://192.168.1.19:3000/api/v1/articles",report);
    }

    private void initTags() {
            languageList = dummyDataForLanguages();
            selectedLanguages = new HashSet<>();
            languageSelectTv = (TextView) findViewById(R.id.tv_preferences_industry_select);
            languageTv = (TextView) findViewById(R.id.tv_preferences_industry);

            final List<CheckedItem> languageDummy = populateLanguageDummy();
            setLanguages();
            languageSelectTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogForSelectingLanguages(languageDummy);
                }
            });
        }

    private void setLanguages() {
        boolean isFirst = true;
        String result = "";
        for (String language : selectedLanguages) {
            if (!isFirst)
                result = result + "   " + language;
            else {
                result = result + language;
                isFirst = false;
            }
        }
        languageTv.setText(result);
    }

    private void showDialogForSelectingLanguages(final List<CheckedItem> checkedItemList) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_preferences_language);
        dialog.setCancelable(false);

        Button confrim = (Button) dialog.findViewById(R.id.btn_multiselect_confirm);
        TypefaceHelper.typeface(confrim);
        TypefaceHelper.typeface(dialog.findViewById(R.id.tv_dialog_language_title));

        ListView listView = (ListView) dialog.findViewById(R.id.lv_dialog_prefs_languages);

        final MultiSelectAdapter languagesAdapter = new MultiSelectAdapter(this, checkedItemList);
        listView.setAdapter(languagesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CheckedItem checkedItemSelectAll = (CheckedItem) parent.getAdapter().getItem(0);
                final CheckedItem checkedItem = (CheckedItem) parent.getAdapter().getItem(position);
                if (position != 0) {
                    Logger.logError("position is not select all, item is checked? " + checkedItem.isChecked);
                    checkedItemSelectAll.isChecked = false;
                    if (checkedItem.isChecked) {
                        checkedItem.isChecked = false;
                    } else {
                        checkedItem.isChecked = true;
                    }
                    Logger.logError("verifying adapter item is checked afer switching? " + ((CheckedItem) parent.getAdapter().getItem(position)).isChecked);
                } else {//checked item is firs element that is select all
                    if (checkedItem.isChecked) {
                        //make all items deselcted
                        for (CheckedItem item : checkedItemList) {
                            item.isChecked = false;
                        }
                        checkedItem.isChecked = false;
                    } else {
                        //make all items as selected
                        for (CheckedItem item : checkedItemList) {
                            item.isChecked = true;
                        }
                        checkedItem.isChecked = true;
                    }
                }
                //redraw view to see effect i.e magic happens here
                languagesAdapter.notifyDataSetChanged();
            }
        });

        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLanguagesDialogDismised(languagesAdapter);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onLanguagesDialogDismised(MultiSelectAdapter adapter) {
        List<CheckedItem> checkedItems = adapter.checkedItems;
        Logger.logError("adapteritems  :" + checkedItems);
        for (int i = 1; i < checkedItems.size(); i++) {
            if (checkedItems.get(i).isChecked)
                selectedLanguages.add(checkedItems.get(i).name);
            else
                selectedLanguages.remove(checkedItems.get(i).name);
        }
        setLanguages();
    }

        private List<CheckedItem> populateLanguageDummy() {
            List<CheckedItem> checkedItemList = new ArrayList<>();
            checkedItemList.add(new CheckedItem("Select All", true));
            checkedItemList.add(new CheckedItem("GTA", true));
            checkedItemList.add(new CheckedItem("Theft", true));
            checkedItemList.add(new CheckedItem("Swacch Bharat", true));
            checkedItemList.add(new CheckedItem("Narendar Modi", true));
            checkedItemList.add(new CheckedItem("Bangalore Police", true));
            checkedItemList.add(new CheckedItem("Bangalore Public Transport", true));
            checkedItemList.add(new CheckedItem("Bangalore CM office", true));
            checkedItemList.add(new CheckedItem("Poluution", true));
            checkedItemList.add(new CheckedItem("Traffic Police", true));
            checkedItemList.add(new CheckedItem("Fire", true));
            checkedItemList.add(new CheckedItem("Drugs", true));
            checkedItemList.add(new CheckedItem("Guns", true));
            checkedItemList.add(new CheckedItem("Bomb", true));


            return checkedItemList;
        }

    private List<String> dummyDataForLanguages() {
        List<String> checkedItemList = new ArrayList<>();
            checkedItemList.add("GTA");
            checkedItemList.add("Theft");
            checkedItemList.add("Swacch Bharat");
            checkedItemList.add("Narendar Modi");
            checkedItemList.add("Bangalore Police");
            checkedItemList.add("Bangalore Public Transport");
            checkedItemList.add("Bangalore CM office");
            checkedItemList.add("Traffic Police");
            checkedItemList.add("Fire");
            checkedItemList.add("Drugs");
            checkedItemList.add("Guns");
            checkedItemList.add("Bomb");
        return checkedItemList;
    }

    private class CheckedItem {
        public String name;
        public boolean isChecked;

        public CheckedItem(String name, boolean isChecked) {
            this.name = name;
            this.isChecked = isChecked;
        }

        @Override
        public String toString() {
            return "CheckedItem{" +
                    "name='" + name + '\'' +
                    ", isChecked=" + isChecked +
                    '}';
        }
    }

    private class MultiSelectAdapter extends BaseAdapter {
        private Activity activity;
        private List<CheckedItem> checkedItems;

        public MultiSelectAdapter(Activity activity, List<CheckedItem> checkedItems) {
            this.activity = activity;
            this.checkedItems = checkedItems;
        }

        @Override
        public int getCount() {
            return checkedItems.size();
        }

        @Override
        public Object getItem(int position) {
            return checkedItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Logger.logError("in get view of multislect adapter!");
            convertView = activity.getLayoutInflater().inflate(R.layout.row_multiselect_string, null);
            init(convertView, position);
            return convertView;
        }

        private void init(View convertView, int position) {
            final CheckedItem item = checkedItems.get(position);
            CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.text1);
            textView.setChecked(item.isChecked);
            textView.setText(item.name);
            TypefaceHelper.typeface(textView);
        }
    }


    private void initLocation() {
        map = (ImageView) findViewById(R.id.iv_report_map);
        locationActv = (AutoCompleteTextView) findViewById(R.id.actv_location_picker);
        locationActv.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.row_placed_autocomplete_item));
        locationActv.setOnItemClickListener(this);
        locationActv.performClick();
    }

    private void initIndustry() {
        reportTitle = (EditText) findViewById(R.id.et_add_report_title);
        categorySpinner = (Spinner) findViewById(R.id.spinner_add_report_category);
        List<String> industries = new ArrayList<>();
        populateDummyIndustries(industries);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, industries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        categorySpinner.setAdapter(dataAdapter);

    }

    private List<String> populateDummyIndustries(List<String> categoryList) {
        categoryList.add("Theft");
        categoryList.add("Mugging");
        categoryList.add("Kidnapping");
        categoryList.add("Murder");
        categoryList.add("Homicide");
        categoryList.add("Traffic violations");
        categoryList.add("Murder");
        return categoryList;
    }

    private void initReportImage() {
        reportImage = (ImageView) findViewById(R.id.iv_add_report);
        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void fileUpload(byte[] image){
        Log.e("report","fileUpload");
        // Create the ParseFile
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new Timestamp(time);
        String ts =  tsTemp.toString();
        Log.e("report","fileUpload size "+image.length);
        final ParseFile file = new ParseFile("report.png", image);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e("file path", "url " + file.getUrl());
                if (e != null)
                    Log.e("Exception", "eception " + e);
                else {
                    Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                    saveImageUpload(file);
                }
            }
        });

    }

   private void saveImageUpload(ParseFile file){
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add(file.getUrl());
        report.setImgUrls(imgUrls);

        ParseObject imgupload = new ParseObject("ImageUpload");
        imgupload.put("ImageName", "Report Image");
        imgupload.put("ImageFile", file);
        imgupload.saveInBackground();
    }

    // file upload

    private void pickImage() {
        onGallerySelected();
    }

    private void onGallerySelected() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == 111 ) && (resultCode == RESULT_OK
                    && null != data)) {
                    loadImageFromGallery(data);
            }
        } catch (Exception e) {
            Log.e("", "Exception " + e);
        }
    }

    private void loadImageFromGallery(Intent data) {

        Log.e("Report", "loadImageFromGallery ");
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap =BitmapFactory
                .decodeFile(imgDecodableString);
        setLoadedImageInImage(bitmap);

        fileUpload(getByteArray(bitmap));
    }

    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    private void setLoadedImageInImage(Bitmap imgDecodableString) {
        reportImage.setImageBitmap(imgDecodableString);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String str = (String) parent.getItemAtPosition(position);
        new AsyncTask<Object,Object,Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startLoader("Getting location details", "Please Wait...");
            }

            @Override
            protected Object doInBackground(Object[] params) {

                Double[] latLong =  getLatLong(str);
                Logger.logError("Latitude and longitude as: " +latLong[0]+ " , "+latLong[1]);
                report.setLattitude(latLong[0]);
                report.setLongitude(latLong[1]);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                stopLoader();
                map.setVisibility(View.VISIBLE);
                String mapUrl ="https://maps.googleapis.com/maps/api/staticmap?center="+report.getLattitude()+","+report.getLongitude()+"&zoom=14&size=500x400&markers=color:red%7C"+report.getLattitude()+","+report.getLongitude()+"&key=AIzaSyAjzh2HxRAWmzb73LpSxarQsaOy6rHWHTk";
                Logger.logError("MAP URL:: " + mapUrl);
                Picasso.with(getApplicationContext()).load(mapUrl).placeholder(R.drawable.location).into(map);
            }
        }.execute();
    }



    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            Filter filter = createFilter();
            return filter;
        }

        private Filter createFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            resultList = autocomplete(constraint.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }

    public ArrayList autocomplete(String input) throws IOException, JSONException {
        ArrayList resultList = null;
        Logger.logError("in autocomplete input = " + input);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            conn = createGooglePlacesApiRequestUrl(input, conn);
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Logger.logError("in first try");
        } catch (MalformedURLException e) {
            Logger.logError("Error processing Places API URL " + e);
            return resultList;
        } catch (IOException e) {
            Logger.logError("Error connecting to Places API " + e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            Logger.logError("JSON as: " + jsonResults.toString());
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            getGooglePlacesApiResultList(resultList, predsJsonArray);
            Logger.logError("result list: " + resultList);
            Logger.logError("in second try");
        } catch (JSONException e) {
            Logger.logError("Cannot process JSON results " + e);
        }

        return resultList;
    }

    private Double[] getLatLong(String address)  {
        Logger.logError("getting lat long for address = " + address);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            conn = createGetLatLongRequestUrl(address, conn);
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Logger.logError("in first try");
        } catch (MalformedURLException e) {
            Logger.logError("Error processing Places API URL " + e);
            return null;
        } catch (IOException e) {
            Logger.logError("Error connecting to Places API " + e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Double[] latLong = new Double[2];

        try {
            Logger.logError("JSON as: " + jsonResults.toString());
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");
            JSONObject geometry = predsJsonArray.getJSONObject(0).getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");;

            latLong[0] = location.getDouble("lat");
            latLong[1] = location.getDouble("lng");

            Logger.logError("LAT and LONG__>>>> " + latLong.toString());
        } catch (JSONException e) {
            Logger.logError("Cannot process JSON results " + e);
        }

        return latLong;
    }

    private static HttpURLConnection createGetLatLongRequestUrl(String address, HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json");
        sb.append("?key=AIzaSyDoscZUBmkqzgZ6Q-5hHJKZ5LMWRCj38-I");
        sb.append("&address=" + URLEncoder.encode(address, "utf8"));
        Logger.logError("URL made= " + sb.toString());
        URL url = new URL(sb.toString());
        conn = (HttpURLConnection) url.openConnection();
        return conn;
    }

    private void getGooglePlacesApiResultList(ArrayList resultList, JSONArray predsJsonArray) throws JSONException {
        for (int i = 0; i < predsJsonArray.length(); i++) {
            System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
            Logger.logError("result: " + predsJsonArray.getJSONObject(i).getString("description"));
            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
        }
    }
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY ="AIzaSyDoscZUBmkqzgZ6Q-5hHJKZ5LMWRCj38-I";

    private static HttpURLConnection createGooglePlacesApiRequestUrl(String input, HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        sb.append("?key=" + API_KEY);
        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

        URL url = new URL(sb.toString());
        conn = (HttpURLConnection) url.openConnection();
        return conn;
    }


    public void startLoader(String message,String title) {
        barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle(title);
        barProgressDialog.setMessage(message);
        barProgressDialog.setCancelable(false);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.show();
    }

    protected  void stopLoader(){
        if(barProgressDialog!=null) barProgressDialog.dismiss();
    }

    public Gson getDateCompatibleGson() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }

        });
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(final Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.getTime());
            }
        });

        Gson gson = builder.create();
        return gson;
    }

    private void saveReport(final String url,final Report report){
        new AsyncTask<String, String, String>() {
            String response = null;
            @Override
            protected String doInBackground(String... params) {
                try {
                    String body = getDateCompatibleGson().toJson(report);
                    response = GenericService.saveReport(url,body).body().string();
                    if(new JSONObject(response).getBoolean("type")){
                        Toast.makeText(getApplicationContext(),"Successfully saved report!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error while saving report!",Toast.LENGTH_LONG).show();
                    }
                    Logger.logError("Response: "+response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }
        }.execute();
    }


}
