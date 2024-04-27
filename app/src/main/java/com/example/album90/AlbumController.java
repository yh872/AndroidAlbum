package com.example.album90;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class AlbumController extends AppCompatActivity {

    public static Album currentAlbum;
    public static int curPage  = 0;

    private static final int Permission_code = 101;

    private ActivityResultLauncher<String> galleryLauncher;
    private Button addButton;
    private Button backButton;
    private Button moveButton;
    private Button displayButton;
    private Button deleteButton;
    private Button next_button;
    private Button previous_button;
    public ImageView img1, img2, img3, img4, img5, img6;
    private boolean img1empty, img2empty, img3empty, img4empty, img5empty, img6empty;

    private Uri imageUri;

    private boolean deletedClicked = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean moveClicked = false;
    private boolean displayClicked = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        curPage = 0;
        img1empty = true; img2empty = true; img3empty = true; img4empty = true; img5empty = true; img6empty = true;
        addButton = findViewById(R.id.Addd);
        backButton = findViewById(R.id.BackButton6);
        moveButton = findViewById(R.id.movv);
        displayButton = findViewById(R.id.disp);
        deleteButton = findViewById(R.id.dell);
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        img5 = findViewById(R.id.image5);
        img6 = findViewById(R.id.image6);
        next_button = findViewById(R.id.next_button);
        previous_button = findViewById(R.id.prevbut);
        previous_button.setEnabled(false);
        if (currentAlbum.numberOfPhotos() < 7){
            next_button.setEnabled(false);
        }
        LoadAlbum(0);
        ImageClicked();
    }


    private void LoadAlbum(int page) {
        img1empty = true; img2empty = true; img3empty = true; img4empty = true; img5empty = true; img6empty = true;
        ArrayList<Photo> photos = currentAlbum.getAllPhotos();

        if (photos.size() >= (page* 6)+ 1) {
            img1.setImageURI(Uri.parse(photos.get(page * 6).path));
            img1empty = false;
        }
        if (photos.size() >=(page* 6) + 2) {
            img2.setImageURI(Uri.parse(photos.get((page * 6) + 1).path));
            img2empty = false;
        }
        if (photos.size() >=(page* 6) + 3) {
            img3.setImageURI(Uri.parse(photos.get((page * 6) + 2).path));
            img3empty = false;
        }
        if (photos.size() >= (page* 6) + 4) {
            img4.setImageURI(Uri.parse(photos.get((page * 6) + 3).path));
            img4empty = false;
        }
        if (photos.size() >=(page* 6) + 5) {
            img5.setImageURI(Uri.parse(photos.get((page * 6) + 4).path));
            img5empty = false;
        }
        if (photos.size() >=(page* 6) + 6) {
            img6.setImageURI(Uri.parse(photos.get((page * 6) + 5).path));
            img6empty = false;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String name = getFileNameFromUri(uri);

            if (name != null) {
                if (isPhotoAlreadyInCurrentAlbum(name)) {
                    showErrorDialog("Photo already exists in this album.");
                } else if (isPhotoInAnotherAlbum(name)) {
                    showErrorDialog("This photo is already located in another album.");
                } else {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        FileOutputStream outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                        int z;
                        while ((z = inputStream.read()) != -1) {
                            outputStream.write(z);
                        }
                        outputStream.close();
                        inputStream.close();

                        Photo newPhoto = new Photo(getFilesDir() + File.separator + name);
                        currentAlbum.addPhoto(newPhoto);

                        Photos.WriteToFile();
                        Toast.makeText(this, "Image successfully added", Toast.LENGTH_SHORT).show();
                        LoadAlbum(curPage);
                        if (currentAlbum.numberOfPhotos() >= (curPage * 6) + 7){
                            next_button.setEnabled(true);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String[] temp = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor c = getContentResolver().query(uri, temp, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                return c.getString(columnIndex);
            }
        }
        return null;
    }

    private boolean isPhotoAlreadyInCurrentAlbum(String fileName) {
        for (Photo photo : currentAlbum.listofPhotos) {
            String s = new File(photo.path).getName();
            if (s.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPhotoInAnotherAlbum(String fileName) {
        for (Album a : User.listofAlbums) {
            for (Photo photo : a.listofPhotos) {
                String s = new File(photo.path).getName();
                if (s.equals(fileName) && !a.equals(currentAlbum)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void addClicked(View view){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void BackButton(View view){
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);
    }

    public void deleted_clicked(View view) {
        if (currentAlbum.numberOfPhotos() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("There are currently no photos in the album.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // You can add any action you want here when the user clicks OK
                }
            });
            builder.show();
            return;
        }
        deletedClicked = true;
        moveClicked = false;
        displayClicked = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image");
        builder.setMessage("Please click the image you would like to delete.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void move_clicked(View view){
        if (currentAlbum.numberOfPhotos() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("There are currently no photos in the album.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return;
        }
        if (User.listofAlbums.size() <2){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("No other album found.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return;

        }
        deletedClicked = false;
        moveClicked = true;
        displayClicked = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Move Photo");
        builder.setMessage("Please click the image you would like to move to another album.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void display_clicked(View view){
        if (currentAlbum.numberOfPhotos() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("There are currently no photos in the album.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return;
        }
        deletedClicked = false;
        moveClicked = false;
        displayClicked = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Display Photo");
        builder.setMessage("Please click the image you would like to Display.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
    public void ImageClicked(){
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img1empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = curPage * 6;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = curPage * 6;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index = 0;
                    DisplayController.index = index;
                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img2empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = (curPage * 6) + 1;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = (curPage * 6) + 1;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index = 1;
                    DisplayController.index = index;

                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img3empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = (curPage * 6) + 2;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = (curPage * 6) + 2;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index = 2;
                    DisplayController.index = index;
                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img4empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = (curPage * 6) + 3;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = (curPage * 6) + 3;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index = 3;
                    DisplayController.index = index;

                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img5empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = (curPage * 6) + 4;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = (curPage * 6) + 4;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index =  4;
                    DisplayController.index = index;

                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img6empty){
                    return;
                }
                if (deletedClicked){
                    deletedClicked = false;
                    int index = (curPage * 6) + 5;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumController.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this photo?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentAlbum.getAllPhotos().remove(index);
                            if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
                                next_button.setEnabled(false);
                            }
                            try {
                                Photos.WriteToFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AlbumController.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                            img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
                            img6.setImageURI(null);
                            LoadAlbum(curPage);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
                else if (moveClicked){
                    int index = (curPage * 6) + 5;
                    moveClicked = false;
                    MoveController.photo = currentAlbum.getAllPhotos().get(index);
                    Intent intent = new Intent(AlbumController.this, MoveController.class);
                    startActivity(intent);

                }
                else if (displayClicked){
                    displayClicked = false;
                    int index =  5;
                    DisplayController.index = index;

                    Intent intent = new Intent(AlbumController.this, DisplayController.class);
                    startActivity(intent);

                }
            }
        });
    }
    public void nextClicked(View view){
        img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
        img6.setImageURI(null);
        curPage++;
        previous_button.setEnabled(true);
        LoadAlbum(curPage);
        if (currentAlbum.numberOfPhotos() < (curPage * 6) + 7){
            next_button.setEnabled(false);
        }

    }

    public void previousClicked(View view){
        img1.setImageURI(null); img2.setImageURI(null); img3.setImageURI(null); img4.setImageURI(null); img5.setImageURI(null);
        img6.setImageURI(null);
        curPage--;
        LoadAlbum(curPage);
        if (curPage == 0){
            previous_button.setEnabled(false);
        }
        if (currentAlbum.numberOfPhotos() >= (curPage * 6) + 7){
            next_button.setEnabled(true);
        }

    }



}