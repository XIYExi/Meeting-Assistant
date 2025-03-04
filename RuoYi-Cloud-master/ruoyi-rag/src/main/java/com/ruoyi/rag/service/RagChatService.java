package com.ruoyi.rag.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface RagChatService {
    String sendFluxMsg(String uid, String text, String question) throws Exception;
}
