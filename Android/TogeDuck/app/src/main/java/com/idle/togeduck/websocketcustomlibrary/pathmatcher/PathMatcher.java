package com.idle.togeduck.websocketcustomlibrary.pathmatcher;

import com.idle.togeduck.websocketcustomlibrary.dto.StompMessage;

public interface PathMatcher {

    boolean matches(String path, StompMessage msg);
}
