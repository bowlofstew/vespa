// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.search.grouping.request.parser;

import com.yahoo.javacc.FastCharStream;

/**
 * @author <a href="mailto:simon@yahoo-inc.com">Simon Thoresen</a>
 */
public class GroupingParserInput extends FastCharStream implements CharStream {

    public GroupingParserInput(String input) {
        super(input);
    }
}
