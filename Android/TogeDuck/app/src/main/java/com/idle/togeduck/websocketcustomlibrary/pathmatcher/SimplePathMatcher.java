package com.idle.togeduck.websocketcustomlibrary.pathmatcher;


import com.idle.togeduck.websocketcustomlibrary.dto.StompHeader;
import com.idle.togeduck.websocketcustomlibrary.dto.StompMessage;


public class SimplePathMatcher implements PathMatcher {

    @Override
    public boolean matches(String path, StompMessage msg) {
        String dest = msg.findHeader(StompHeader.DESTINATION);
        if (dest == null) return false;
        else return path.equals(dest);
    }
}
