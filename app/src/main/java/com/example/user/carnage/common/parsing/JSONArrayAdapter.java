package com.example.user.carnage.common.parsing;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.EmptyStackException;

class JSONArrayAdapter {
    private final JSONArray array;

    JSONArrayAdapter(JSONArray array) {
        this.array = array;
    }

    <T> Iterator iterator() {
        return new Iterator<T>(array);
    }

    static class Iterator<T> {
        private final JSONArray array;
        private final int length;
        private int pos = 0;

        private Iterator(JSONArray array) {
            this.array = array;
            length = array.length();
        }

        @SuppressWarnings("unchecked")
        T next() throws JSONException, EmptyStackException {
            if (hasNext()) return (T) array.get(++pos);
            else throw new EmptyStackException();
        }

        boolean hasNext() {
            return pos < length -1;
        }
    }
}
