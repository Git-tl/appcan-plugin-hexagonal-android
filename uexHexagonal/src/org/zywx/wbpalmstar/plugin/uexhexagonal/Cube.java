/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zywx.wbpalmstar.plugin.uexhexagonal;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Pair;

/**
 * A vertex shaded cube.
 */
class Cube {
	private Context mContext = null;
	private List<Pair<String, String>> mParam;
	private EUExBase mEUExBase;
	public static final String function_openhexagonal = "uexHexagonal.cbOpenHexagonal";
	private int one = 0x10000;
	private int vertices[][] = { new int[] { (int) (-0.5f * one), (int) (0.9f * one), (int) (0.816f * one), // 0
			(int) (0.5f * one), (int) (0.9f * one), (int) (0.816f * one), // 1
			(int) (-one), (int) (0.9f * one), (int) (0), // 2
			(int) (one), (int) (0.9f * one), (int) (0), // 3
			(int) (-0.5f * one), (int) (0.9f * one), (int) (-0.816f * one), // 4
			(int) (0.5f * one), (int) (0.9f * one), (int) (-0.816f * one) // 5
			}, new int[] { (int) (-0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 6
					(int) (0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 7
					(int) (-one), (int) (-0.9f * one), (int) (0), // 8
					(int) (one), (int) (-0.9f * one), (int) (0), // 9
					(int) (-0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one), // 10
					(int) (0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one) // 11
			}, new int[] { (int) (-0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 6
					(int) (0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 7
					(int) (-0.5f * one), (int) (0.9f * one), (int) (0.816f * one), // 0
					(int) (0.5f * one), (int) (0.9f * one), (int) (0.816f * one) // 1
			}, new int[] { (int) (-one), (int) (-0.9f * one), (int) (0), // 8
					(int) (-0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 6
					(int) (-one), (int) (0.9f * one), (int) (0), // 2
					(int) (-0.5f * one), (int) (0.9f * one), (int) (0.816f * one) // 0
			}, new int[] { (int) (-0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one), // 10
					(int) (-one), (int) (-0.9f * one), (int) (0), // 8
					(int) (-0.5f * one), (int) (0.9f * one), (int) (-0.816f * one), // 4
					(int) (-one), (int) (0.9f * one), (int) (0) // 2
			}, new int[] { (int) (-0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one), // 10
					(int) (0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one), // 11
					(int) (-0.5f * one), (int) (0.9f * one), (int) (-0.816f * one), // 4
					(int) (0.5f * one), (int) (0.9f * one), (int) (-0.816f * one) // 5
			}, new int[] { (int) (0.5f * one), (int) (-0.9f * one), (int) (-0.816f * one), // 11
					(int) (one), (int) (-0.9f * one), (int) (0), // 9
					(int) (0.5f * one), (int) (0.9f * one), (int) (-0.816f * one), // 5
					(int) (one), (int) (0.9f * one), (int) (0), // 3
			}, new int[] { (int) (one), (int) (-0.9f * one), (int) (0), // 9
					(int) (0.5f * one), (int) (-0.9f * one), (int) (0.816f * one), // 7
					(int) (one), (int) (0.9f * one), (int) (0), // 3
					(int) (0.5f * one), (int) (0.9f * one), (int) (0.816f * one) // 1
			} };

	private int colors[] = { 0, 0, 0, one, one, 0, 0, one, one, one, 0, one, 0, one, 0, one, 0, 0, one, one, one, 0,
			one, one, one, one, one, one, 0, one, one, one, };

	private int[] textures = new int[8];
	private float[][] texture = { new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f,

	}, new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f,

	}, new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

	}, new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

	}, new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

	}, new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

	}, new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

	}, new float[] { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

	} };

	private byte indices[][] = { new byte[] { 0, 2, 1, 1, 2, 3, 3, 2, 4, 3, 4, 5 },
			new byte[] { 0, 1, 2, 1, 3, 2, 3, 4, 2, 3, 5, 4 }, new byte[] { 2, 1, 0, 2, 3, 1 }, // 6
																								// 7
																								// 0
																								// 1
			new byte[] { 2, 1, 0, 2, 3, 1 }, // 8 6 2 0
			new byte[] { 3, 1, 0, 2, 3, 0 }, // 10 8 4 2
			new byte[] { 2, 0, 1, 2, 1, 3 }, // 10 11 4 5
			new byte[] { 2, 0, 1, 2, 1, 3 }, // 11 9 5 3
			new byte[] { 2, 0, 1, 2, 1, 3 }, // 9 7 3 1

	};

	public Cube(Context context, List<Pair<String, String>> param) {
		mContext = context;
		mParam = param;
		// Buffers to be passed to gl*Pointer() functions
		// must be direct, i.e., they must be placed on the
		// native heap where the garbage collector cannot
		// move them.
		//
		// Buffers with multi-byte datatypes (e.g., short, int, float)
		// must have their byte order set to native order
		for (int i = 0; i < vertices.length; i++) {
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices[i].length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer[i] = vbb.asIntBuffer();
			mVertexBuffer[i].put(vertices[i]);
			mVertexBuffer[i].position(0);
		}

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asIntBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);

		for (int i = 0; i < indices.length; i++) {
			mIndexBuffer[i] = ByteBuffer.allocateDirect(indices[i].length);
			mIndexBuffer[i].put(indices[i]);
			mIndexBuffer[i].position(0);
		}

	}

	public Cube(EUExBase base, Context context,
			List<Pair<String, String>> param) {
		this(context, param);
		mEUExBase = base;
	}

	public void init(GL10 gl) {
		if (mContext != null)
			loadTexture(gl, mContext);

		ByteBuffer bb;
		// 初始化保存纹理的FloatBuffer

		textureBuffer = new FloatBuffer[8];
		for (int i = 0; i < 8; i++) {
			bb = ByteBuffer.allocateDirect(texture[i].length * 4);
			bb.order(ByteOrder.nativeOrder());
			textureBuffer[i] = bb.asFloatBuffer();
			textureBuffer[i].put(texture[i]);
			textureBuffer[i].position(0);
		}

	}

	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		for (int i = 0; i < 8; i++) {
			 // 开启设置材质数组
			
			gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer[i]);
			// gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
			// gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texBuff[i]);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
	                GL10.GL_REPEAT);
	        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
	                GL10.GL_REPEAT);
			// Point to our buffers
			// 设置纹理坐标
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer[i]);
			
			gl.glDrawElements(GL10.GL_TRIANGLES, indices[i].length, GL10.GL_UNSIGNED_BYTE, mIndexBuffer[i]);
			//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		}
	}

	public void loadTexture(GL10 gl, Context context) {
		// define the resourcesId
		//EUExUtil.getResRawID(resName)
		int[] resourcesIds = { EUExUtil.getResRawID("plugin_hexagonal_a0"),
				EUExUtil.getResRawID("plugin_hexagonal_a0"), 
				EUExUtil.getResRawID("plugin_hexagonal_a0"),
				EUExUtil.getResRawID("plugin_hexagonal_a1"), 
				EUExUtil.getResRawID("plugin_hexagonal_a2"),
				EUExUtil.getResRawID("plugin_hexagonal_a3"), 
				EUExUtil.getResRawID("plugin_hexagonal_a4"),
				EUExUtil.getResRawID("plugin_hexagonal_a5") };
//		int[] resourcesIds = { EUExUtil.getResRawID("robot"),
//				EUExUtil.getResRawID("robot"), EUExUtil.getResRawID("robot"),
//				EUExUtil.getResRawID("robot"), EUExUtil.getResRawID("robot"),
//				EUExUtil.getResRawID("robot"), EUExUtil.getResRawID("robot"),
//				EUExUtil.getResRawID("robot") };
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glGenTextures(8, textures, 0);
		
		// Get the texture from the Android resource directory
		for (int i = 0; i < 8; i++) {
			Bitmap bitmap = null;
			int j = i;
			if (i >= 3) {
				j = 7-(i-3);
			}
			if(null != mParam){
				bitmap = loadBitmapFromRes(context, mParam.get(j).first);
			}else{
				bitmap = loadBitmap(context, resourcesIds[j]);
			}
			if (null == bitmap) {
				mEUExBase.jsCallback(function_openhexagonal, 0, EUExCallback.F_C_INT, 1);
				return;
			}
			// Generate one texture pointer...
			// ...and bind it to our array
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);

			// Create Nearest Filtered Texture
			 gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
		                GL10.GL_NEAREST);
		        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
		                GL10.GL_TEXTURE_MAG_FILTER,
		                GL10.GL_LINEAR);

		        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
		                GL10.GL_CLAMP_TO_EDGE);
		        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
		                GL10.GL_CLAMP_TO_EDGE);

		        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
		                GL10.GL_REPLACE);
			// Use the Android GLUtils to specify a two-dimensional texture
			// image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			// Clean up
			bitmap.recycle();
		}
	}

	public Bitmap loadBitmap(Context context, int resourceid) {

		if (context == null)
			return null;
		InputStream is = context.getResources().openRawResource(resourceid);
		try {
			// BitmapFactory is an Android graphics utility for images
			return BitmapFactory.decodeStream(is);
		} finally {
			// Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
	}
	
	public Bitmap loadBitmapFromRes(Context context, String imagePath) {
		if(null == imagePath){
			return null;
		}
		Bitmap result = null;
		InputStream in = null;
		try{
			if(null != imagePath && 0 != imagePath.length()){
				if(imagePath.startsWith("/sdcard")){
					File file = new File(imagePath);
					in = new FileInputStream(file);
				}else if(imagePath.startsWith("widget/")){
					AssetManager asm = context.getAssets();
					in = asm.open(imagePath);
				}else if(imagePath.startsWith("/data/data")){
					File file = new File(imagePath);
					in = new FileInputStream(file);
				}else{
					File file = new File(imagePath);
					in = new FileInputStream(file);
				}
				if(null != in){
					result = BitmapFactory.decodeStream(in);
					in.close();
					return result;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (Exception e) {
					;
				}
			}
		}
		return null;
	}

	private IntBuffer[] mVertexBuffer = new IntBuffer[8];
	private IntBuffer mColorBuffer;
	private ByteBuffer[] mIndexBuffer = new ByteBuffer[8];
	private FloatBuffer[] textureBuffer;
}

// class Cube
// {
// public Cube()
// {
// int one = 0x10000;
// int vertices[] = {
// (int) (-0.5f*one),one,(int) ( 0.816f*one), //0
// (int) ( 0.5f*one),one,(int) ( 0.816f*one), //1
// (int) ( -one),one,(int) (0), //2
// (int) ( one),one,(int) (0), //3
// (int) (-0.5f*one),one,(int) (-0.816f*one), //4
// (int) ( 0.5f*one),one,(int) (-0.816f*one), //5
//
// (int) (-0.5f*one),-one,(int) ( 0.816f*one), //6
// (int) ( 0.5f*one),-one,(int) ( 0.816f*one), //7
// (int) ( -one),-one,(int) (0), //8
// (int) ( one),-one,(int) (0), //9
// (int) (-0.5f*one),-one,(int) (-0.816f*one), //10
// (int) ( 0.5f*one),-one,(int) (-0.816f*one), //11
//
// };
//
// int colors[] = {
// 0, 0, 0, one,
// one, 0, 0, one,
// one, one, 0, one,
// 0, one, 0, one,
// 0, 0, one, one,
// one, 0, one, one,
// one, one, one, one,
// 0, one, one, one,
// 0, (int) (0.5f*one), one, (int) (0.5f*one),
// 0, (int) (0.5f*one), (int) (0.5f*one), one,
// };
//
// byte indices[] = {
// 0,7,6, 0,1,7,
// 2,6,8, 2,0,6,
// 4,8,10, 4,2,8,
// 4,10,11, 4,11,5,
// 5,11,9, 5,9,3,
// 1,9,7, 1,3,9,
//
// 0,2,1, 1,2,3, 3,2,4 ,3,4,5,
// 6,7,8, 7,9,8, 9,10,8 ,9,11,10
//
//
// };
//
// // Buffers to be passed to gl*Pointer() functions
// // must be direct, i.e., they must be placed on the
// // native heap where the garbage collector cannot
// // move them.
// //
// // Buffers with multi-byte datatypes (e.g., short, int, float)
// // must have their byte order set to native order
//
// ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
// vbb.order(ByteOrder.nativeOrder());
// mVertexBuffer = vbb.asIntBuffer();
// mVertexBuffer.put(vertices);
// mVertexBuffer.position(0);
//
// ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
// cbb.order(ByteOrder.nativeOrder());
// mColorBuffer = cbb.asIntBuffer();
// mColorBuffer.put(colors);
// mColorBuffer.position(0);
//
// mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
// mIndexBuffer.put(indices);
// mIndexBuffer.position(0);
// }
//
// public void draw(GL10 gl)
// {
// gl.glFrontFace(gl.GL_CW);
// gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
// gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
// gl.glDrawElements(gl.GL_TRIANGLES, 60, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
// }
//
// private IntBuffer mVertexBuffer;
// private IntBuffer mColorBuffer;
// private ByteBuffer mIndexBuffer;
// }

// class Cube
// {
// public Cube()
// {
// int one = 0x10000;
// int vertices[] = {
// -one, -one, -one,
// one, -one, -one,
// one, one, -one,
// -one, one, -one,
// -one, -one, one,
// one, -one, one,
// one, one, one,
// -one, one, one,
// };
//
// int colors[] = {
// 0, 0, 0, one,
// one, 0, 0, one,
// one, one, 0, one,
// 0, one, 0, one,
// 0, 0, one, one,
// one, 0, one, one,
// one, one, one, one,
// 0, one, one, one,
// };
//
// byte indices[] = {
// 0, 4, 5, 0, 5, 1,
// 1, 5, 6, 1, 6, 2,
// 2, 6, 7, 2, 7, 3,
// 3, 7, 4, 3, 4, 0,
// 4, 7, 6, 4, 6, 5,
// 3, 0, 1, 3, 1, 2
// };
//
// // Buffers to be passed to gl*Pointer() functions
// // must be direct, i.e., they must be placed on the
// // native heap where the garbage collector cannot
// // move them.
// //
// // Buffers with multi-byte datatypes (e.g., short, int, float)
// // must have their byte order set to native order
//
// ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
// vbb.order(ByteOrder.nativeOrder());
// mVertexBuffer = vbb.asIntBuffer();
// mVertexBuffer.put(vertices);
// mVertexBuffer.position(0);
//
// ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
// cbb.order(ByteOrder.nativeOrder());
// mColorBuffer = cbb.asIntBuffer();
// mColorBuffer.put(colors);
// mColorBuffer.position(0);
//
// mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
// mIndexBuffer.put(indices);
// mIndexBuffer.position(0);
// }
//
// public void draw(GL10 gl)
// {
// gl.glFrontFace(gl.GL_CW);
// gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
// gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
// gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
// }
//
// private IntBuffer mVertexBuffer;
// private IntBuffer mColorBuffer;
// private ByteBuffer mIndexBuffer;
// }
