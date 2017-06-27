
package com.smartware.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSASecurity {

	private static final boolean	DEBUG = true;
	private static final String TAG = "RSASecurity";

	private static RSASecurity sInstance;

	private Utils								mUtils = Utils.getInstance();

	private Context mContext;

	public static RSASecurity getInstance() {
		if (sInstance == null) {
			synchronized (RSASecurity.class) {
				if (sInstance == null) {
					sInstance = new RSASecurity();
				}
			}
		}
		return sInstance;
	}

	private RSASecurity() {
	}

	public void setContext(Context context) {
		mContext = context;
	}

	@SuppressLint("TrulyRandom")
	public String RSAEncrypt(String sValue){
		try {
			Document XMLSecKeyDoc  = parseXMLFile(mContext.getAssets().open("publickey.xml"));
			PublicKey pubKey = convertXMLRSAPublicKey(XMLSecKeyDoc);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] 			encrypted = cipher.doFinal(sValue.getBytes("UTF-8"));
			return Base64.encodeToString(encrypted, Base64.DEFAULT);
		} catch (Exception e) {
			mUtils.printLog(  DEBUG, TAG, "[RSAEncrypt] Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Document parseXMLFile(String filename) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new File(filename) );
			return document;
		} catch(Exception e) {
			mUtils.printLog(  DEBUG, TAG, "[parseXMLFile] Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Document parseXMLFile(InputStream is) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);
			return document;
		} catch (Exception e) {
			mUtils.printLog(  DEBUG, TAG, "[parseXMLFile] Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}

	private PublicKey convertXMLRSAPublicKey(Document xmldoc) {
		Node root = xmldoc.getFirstChild();
		NodeList children = root.getChildNodes();
		BigInteger modulus = null;
		BigInteger exponent = null;
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			String textValue = node.getTextContent();
			if (node.getNodeName().equals("Modulus")) {
				modulus = new BigInteger(1, Base64.decode(textValue, 0) );
			} else if (node.getNodeName().equals("Exponent")) {
				exponent = new BigInteger(1, Base64.decode(textValue, 0) );
			}
		}
		try {
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey key = keyFactory.generatePublic(keySpec);
			return key;
		} catch (Exception e) {
			mUtils.printLog(  DEBUG, TAG, "[convertXMLRSAPublicKey] Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
}























