package com.ark.norns.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

public class OidUtil {
    public static final Splitter pointDoublePointSplitter =
            Splitter.on(CharMatcher.anyOf(".:"))
                    .omitEmptyStrings()
                    .trimResults();
}
