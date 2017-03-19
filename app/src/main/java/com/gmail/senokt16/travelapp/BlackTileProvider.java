package com.gmail.senokt16.travelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;

public class BlackTileProvider implements TileProvider {

    Context context;
    byte[] tileData;
    public BlackTileProvider(Context context) {
        this.context = context;
    }

    @Override
    public Tile getTile(int i, int i1, int i2) {
        if (tileData == null) {
            Drawable tileDrawable = ContextCompat.getDrawable(context, R.drawable.black_tile);
            Bitmap b = ((BitmapDrawable) tileDrawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            tileData = stream.toByteArray();
        }
        return new Tile(512, 512, tileData);
    }
}
