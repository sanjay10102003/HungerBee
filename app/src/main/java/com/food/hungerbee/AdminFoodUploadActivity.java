package com.food.hungerbee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.food.hungerbee.ModelClasses.FoodModelClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminFoodUploadActivity extends AppCompatActivity {
    Spinner categorySpinner;
    private static final String[] CategoryList = {"","Pizza","Burger","shawarma","Fresh Juice"};
    Button btnUploadItem;
    EditText edtItemName,edtItemPrice;
    ImageView imgItem;
    public Uri imageUri;
    String StringimageUrl;
    DatabaseReference AdmindatabaseReference,databaseReferenceUsers;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_upload);
        Spinners();

        AdmindatabaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        edtItemName = findViewById(R.id.edtItemName);
        edtItemPrice = findViewById(R.id.edtItemPrice);
        imgItem = findViewById(R.id.imgItem);
        btnUploadItem = findViewById(R.id.btnUploadItem);
        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnUploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateItemName() | !ValidateItemPrice() | !ValidateCategory() | imageUri == null){
                    return;
                }
                String ItemName = edtItemName.getText().toString();
                String ItemPrice = edtItemPrice.getText().toString();
                String Category = categorySpinner.getSelectedItem().toString();
                uploadPicture(imageUri,ItemName,ItemPrice,Category);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imgItem.setImageURI(imageUri);
        }/*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(userpic);
            }
        }*/
    }

    private void uploadPicture(Uri imageUri,String ItemName,String ItemPrice,String Category) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.setCancelable(false);
        pd.show();
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                StringimageUrl = uri.toString();
                                String UserIdString = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                String ItemId = AdmindatabaseReference.child(UserIdString).child("Foods").push().getKey();
                                FoodModelClass foodModelClass = new FoodModelClass(StringimageUrl,ItemId,ItemName,ItemPrice,Category,UserIdString);
                                AdmindatabaseReference.child(UserIdString).child("Foods").child(ItemId).setValue(foodModelClass);
                                Toast.makeText(AdminFoodUploadActivity.this, "Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                Intent intent = new Intent(getApplicationContext(),AdminMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminFoodUploadActivity.this, "Uploading falied! please try again...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot){
                double onProcessPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) onProcessPercent+"%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminFoodUploadActivity.this, "Uploading falied! please try again...", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }
    private String getFileExtension(Uri imageUri){
        //to return the type like the image is png or jpg etc.,
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

    private Boolean ValidateItemName() {
        String value = edtItemName.getText().toString();
        if (value.isEmpty()) {
            edtItemName.setError("This Field must be Non empty");
            return false;
        } else {
            edtItemName.setError(null);
            return true;
        }
    }
    private Boolean ValidateItemPrice() {
        String value = edtItemPrice.getText().toString();
        if (value.isEmpty()) {
            edtItemPrice.setError("This Field must be Non empty");
            return false;
        } else {
            edtItemPrice.setError(null);
            return true;
        }
    }
    private Boolean ValidateCategory() {
        String value = categorySpinner.getSelectedItem().toString();
        if (value.isEmpty()) {
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            return false;
        } else {
            return true;
        }
    }


    private void Spinners() {
        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,CategoryList);
        categorySpinner.setAdapter(CategoryAdapter);
    }
}