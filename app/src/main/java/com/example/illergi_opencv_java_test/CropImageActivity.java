package com.example.illergi_opencv_java_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.illergi_opencv_java_test.entities.FoodData;
import com.example.illergi_opencv_java_test.entities.UserAllergyFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2Lab;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2BGR;
import static org.opencv.imgproc.Imgproc.COLOR_Lab2BGR;

public class CropImageActivity extends AppCompatActivity {
    static {
        if (OpenCVLoader.initDebug()) System.err.println("successed");
        else System.err.println("unsuccessed");
    }

    private static String TAG = "CropImageActivity";

    Button selectButton;
    ImageButton backArrow;
    CropImageView cropImageView;
    ArrayList<String> allergicFoodList = new ArrayList<String>();
    final String[] foodName = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop);

        selectButton = findViewById(R.id.Select);
        backArrow = findViewById(R.id.backArrow);
        cropImageView = findViewById(R.id.cropImageView);


        Intent intent = getIntent();
        String imagePath = intent.getExtras().getString("ImagePath");

        File imageFile = new File(imagePath);
        if(imageFile.exists())
        {
            cropImageView.setImageUriAsync(Uri.fromFile(imageFile));
        }

        getAllergicFoodList();

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedImage = cropImageView.getCroppedImage();

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                File croppedImagePath = new File(Environment.getExternalStorageDirectory() + "/Pictures/iLLergi" + "/" + ts + ".jpg");

//                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                try {
                    FileOutputStream croppedImagePathStream = new FileOutputStream(croppedImagePath);
                    croppedImage.compress(Bitmap.CompressFormat.PNG, 90, croppedImagePathStream);
                    croppedImagePathStream.flush();
                    croppedImagePathStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String Result = "";


//                image processing
                Mat img = new Mat();
                Utils.bitmapToMat(croppedImage,img);

                Mat hsv_img = new Mat();
                Imgproc.cvtColor(img, hsv_img, COLOR_BGR2HSV);
                writeImage("hsv_img.jpg", hsv_img);

                Mat frame_threshed = new Mat();
                Core.inRange(hsv_img, new Scalar(0, 0, 50),new Scalar(0, 0, 225),frame_threshed);
                writeImage("frame_threshed.jpg", frame_threshed);

                Imgproc.cvtColor(img, img, COLOR_BGRA2BGR);
                Mat result1 = new Mat();
                Photo.inpaint(img,frame_threshed,result1,0.1,Photo.INPAINT_TELEA);
                writeImage("result1.jpg", result1);

                Mat lab = new Mat();
                Imgproc.cvtColor(result1,lab,COLOR_BGR2Lab);
                writeImage("lab.jpg", lab);

                List<Mat> lab_planes = new ArrayList<>();
                Core.split(lab,lab_planes);

                CLAHE clahe = Imgproc.createCLAHE();
                clahe.setClipLimit(2.0);
                clahe.setTilesGridSize(new Size(8,8));

                Mat dst = new Mat();
                clahe.apply(lab_planes.get(0),dst);
                lab_planes.set(0,dst);

                Core.merge(lab_planes,lab);
                writeImage("lab_merge.jpg", lab); //ภาพจะเข้มขึ้น

                Mat clahe_bgr = new Mat();
                Imgproc.cvtColor(lab,clahe_bgr,COLOR_Lab2BGR);
                writeImage("clahe_bgr.jpg", clahe_bgr);



                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(toBitmap(clahe_bgr)).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    //get text from sb until there is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
//                        sb.append("\n");
                    }
                    String mydata[] = sb.toString().toLowerCase().replaceAll("(\\t|\\r?\\n)+", " ")
                            .replace(" or ",",").replace(" and ",",").replace(" contain ",",")
                            .replace(" contain ",",").replace(" contains ",",").replace(" containing ",",")
                            .replace(" including ",",").replace(" traces of ",",").replace(" other ",",")
                            .replace(" with ",",").replace(" of ",",").replace("-","").replace(" a ","")
                            .replace(" an ","").replace(" the ","")
                            .split("[,(.:%&;)]+",-1);

                    // To reduce Redundant words
                    Set<String> mydata_set = new LinkedHashSet<String>();
                    mydata_set.addAll(Arrays.asList(mydata));

//                    Log.e("CropImageActivity","in the Set");
//                    for(int i=0; i< mydata_set.size(); i++) Log.d("CropImageActivity",mydata_set.toArray()[i].toString()+"  type: "+mydata_set.toArray()[i].getClass().getSimpleName());
//                    Log.e("CropImageActivity","finish the set");
//                    Log.e("CropImageActivity", Integer.toString(mydata_set.size()));
//                    Log.e("CropImageActivity", Integer.toString(mydata.length));

                    Set<String> ingredient_set = new LinkedHashSet<String>();

                    for (int i=0; i<mydata_set.size(); i++){
                        String ind_data = mydata_set.toArray()[i].toString().trim();
                        if(!isNumber(ind_data) && !onlySpecialCharacters((ind_data)) && !ind_data.matches("^\\s*$")){
                            String ingredient = clean_ingredient(ind_data).trim();
                            Log.e("CropImageActivity", ingredient);
                            int num = Integer.MAX_VALUE;
                            String min_allergy = null;

                            for (String foodname : allergicFoodList){
                                int min_edit_dis = levenshtein(ingredient,foodname);

                                if(min_edit_dis < num){
                                    num = min_edit_dis;
                                    min_allergy = foodname;
                                }
                            }

                            int max_dis = Math.max(min_allergy.length(), ingredient.length());
                            if(num < max_dis/2) ingredient_set.add(min_allergy);
                        }
                    }

                    String showResult = "";

                    for(int i=0; i<ingredient_set.size(); i++){
                        if(i != ingredient_set.size()-1) showResult += ingredient_set.toArray()[i].toString()+", ";
                        else showResult += ingredient_set.toArray()[i].toString();
                    }

                    //set text to edit text
                    Result = showResult.toString();
                }

                Intent showResultIntent = new Intent(CropImageActivity.this,ShowResultActivity.class);
                showResultIntent.putExtra("CropImagePath",croppedImagePath.getAbsolutePath());
                showResultIntent.putExtra("SavedImageName",ts+".jpg");
                showResultIntent.putExtra("ResultText",Result);
                showResultIntent.putExtra("Key","1");

               startActivity(showResultIntent);
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    private void writeImage(String name, Mat origin_resize) {

        String appPath = Environment.getExternalStorageDirectory()+"/Pictures/iLLergi/RecognizeTextOCR/";
        info("Writing " + appPath + name + "...");
        File path = new File(appPath);
        if(!path.exists()) {
            path.mkdir();
            info("make dir ");
        }
        Imgcodecs.imwrite(appPath + name, origin_resize);

    }
    public static void info(Object msg) {
        Log.e(TAG, msg.toString());
    }

    public static Bitmap toBitmap(Mat mat) {
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

    public boolean isNumber(String data){
//        try {
//            Double.parseDouble(data);
//            return true;
//
//        }catch(Exception e){
//            return false;
//        }
        String regex = "^[0-9]+$"; //a number
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(data);
        if(!m.matches()) return false; //is String
        else return  true; //is Number

    }

    public static boolean onlySpecialCharacters(String str)
    {

        // Regex to check if a string contains
        // only special characters
        String regex = "[^a-zA-Z0-9]+";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // then print No
        if (str == null) {
            return true;
        }

        // Find match between given string
        // & regular expression
        Matcher m = p.matcher(str);

        // Print Yes If the string matches
        // with the Regex
        if (m.matches())
            return true;
        else
            return false;
    }

    public String clean_ingredient(String ingredient){
        String datas[] = ingredient.split("\\s+");
        StringBuilder sb = new StringBuilder();
//        String regex = "^[0-9]+$";
//        Pattern p = Pattern.compile(regex);


//        for(String data: datas){
//            Matcher m = p.matcher(data);
//            if(!m.matches()) sb.append(data+" ");
//        }
        for(String data: datas){
            if(!isNumber(data)) sb.append(data+" ");
        }
        return sb.toString();
    }

    int levenshtein(String token1, String token2) {

        int[] distances = new int[token1.length() + 1];

        for (int t1 = 1; t1 <= token1.length(); t1++) {
            if (token1.charAt(t1 - 1) == token2.charAt(0)) {
                distances[t1] = calcMin(distances[t1 - 1], t1 - 1, t1);
            } else {
                distances[t1] = calcMin(distances[t1 - 1], t1 - 1, t1) + 1;
            }
        }

        int dist = 0;
        for (int t2 = 1; t2 < token2.length(); t2++) {
            dist = t2 + 1;
            for (int t1 = 1; t1 <= token1.length(); t1++) {
                int tempDist;
                if (token1.charAt(t1 - 1) == token2.charAt(t2)) {
                    tempDist = calcMin(dist, distances[t1 - 1], distances[t1]);
                } else {
                    tempDist = calcMin(dist, distances[t1 - 1], distances[t1]) + 1;
                }
                distances[t1 - 1] = dist;
                dist = tempDist;
            }
            distances[token1.length()] = dist;
        }
        return dist;
    }

    static int calcMin(int a, int b, int c) {
        if (a <= b && a <= c) {
            return a;
        } else if (b <= a && b <= c) {
            return b;
        } else {
            return c;
        }
    }

    private void getAllergicFoodList(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("user_allergy_food").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserAllergyFood data = document.toObject(UserAllergyFood.class);
//                        Log.d("TAG", "DocumentSnapshot data: " + data.toString());
                        List<String> mergeList = data.mergeList();
                        for (String item : mergeList){
                            getFoodName(item);
                            Log.d("TAG", "getFoodName result data: " + foodName[0]);
//                            allergicFoodList.add(foodName[0]);
                        }

                        Log.d("TAG", "DocumentSnapshot data: " + allergicFoodList);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getFoodName(String firestoreId){
            FirebaseFirestore.getInstance().collection("food_list").document(firestoreId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                FoodData data = document.toObject(FoodData.class);
//                                foodName[0] = data.getName();
                                allergicFoodList.add(data.getName());
                                Log.d("TAG", "getFoodName data: " + data.getName());
                                Log.d("TAG", "allergicFoodList data: " + allergicFoodList);
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

    }
}