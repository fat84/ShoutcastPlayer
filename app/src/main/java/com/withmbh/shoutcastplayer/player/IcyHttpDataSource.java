package com.withmbh.shoutcastplayer.player;

import android.util.Log;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Predicate;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * https://cast.readme.io/v1.0/docs/icy
 * http://www.smackfu.com/stuff/programming/shoutcast.html
 */
public final class IcyHttpDataSource extends DefaultHttpDataSource {
    private static final String TAG = IcyHttpDataSource.class.getSimpleName();

    private static final String REQUEST_HEADER_ICY_METAINT_KEY = "Icy-MetaData";
    private static final String REQUEST_HEADER_ICY_METAINT_VALUE = "1";

    private static final String RESPONSE_HEADER_ICY_BR_KEY = "icy-br";
    private static final String RESPONSE_HEADER_ICY_GENRE_KEY = "icy-genre";
    private static final String RESPONSE_HEADER_ICY_NAME_KEY = "icy-name";
    private static final String RESPONSE_HEADER_ICY_URL_KEY = "icy-url";
    private static final String RESPONSE_HEADER_ICY_PUB_KEY = "icy-pub";
    private static final String RESPONSE_HEADER_ICY_METAINT_KEY = "icy-metaint";

    private static final String ICY_METADATA_STREAM_TITLE_KEY = "StreamTitle";
    private static final String ICY_METADATA_STREAM_URL_KEY = "StreamUrl";

    private IcyHeadersListener icyHeadersListener;
    private IcyMetadataListener icyMetadataListener;
    private int metaDataIntervalInBytes = -1;
    private int remainingStreamDataUntilMetaDataBlock = -1;
    private DataSpec dataSpec;

    public interface IcyHeadersListener {
        void onIcyHeaders(IcyHeaders icyHeaders);
    }

    public interface IcyMetadataListener {
        void onIcyMetaData(IcyMetadata icyMetadata);
    }

    private IcyHttpDataSource(
            final String userAgent,
            final Predicate<String> contentTypePredicate,
            final TransferListener<? super DefaultHttpDataSource> listener,
            final int connectTimeoutMillis,
            final int readTimeoutMillis,
            final boolean allowCrossProtocolRedirects,
            final RequestProperties defaultRequestProperties) {
        super(userAgent, contentTypePredicate, listener, connectTimeoutMillis, readTimeoutMillis, allowCrossProtocolRedirects, defaultRequestProperties);
        defaultRequestProperties.set(REQUEST_HEADER_ICY_METAINT_KEY, REQUEST_HEADER_ICY_METAINT_VALUE);
        // See class Builder
    }

    @Override
    public long open(DataSpec dataSpec) throws HttpDataSourceException {
        this.dataSpec = dataSpec;
        long bytesToRead = super.open(dataSpec);

        Map<String, List<String>> responseHeaders = getResponseHeaders();
        if (responseHeaders != null) {
            IcyHeaders icyHeaders = new IcyHeaders();

            Log.d(TAG, "open: responseHeaders=" + responseHeaders.toString());
            List<String> headers = responseHeaders.get(RESPONSE_HEADER_ICY_BR_KEY);
            if (headers != null && headers.size() == 1) {
                icyHeaders.bitRate = Integer.parseInt(headers.get(0));
            }
            headers = responseHeaders.get(RESPONSE_HEADER_ICY_GENRE_KEY);
            if (headers != null && headers.size() == 1) {
                icyHeaders.genre = headers.get(0);

                //byte[] euckrStringBuffer = helloString.getBytes(Charset.forName("euc-kr"));


            }
            headers = responseHeaders.get(RESPONSE_HEADER_ICY_NAME_KEY);
            if (headers != null && headers.size() == 1) {
                //icyHeaders.name = headers.get(0);

                String originalStr =  "아름다운 풍경소리"; // "아름다운 풍경소리"
                String [] charSet = {"utf-8","euc-kr","ksc5601","iso-8859-1","x-windows-949"};

                for (int i=0; i<charSet.length; i++) {
                    for (int j=0; j<charSet.length; j++) {
                        try {
                            Log.i(TAG, "[" + charSet[i] +"," + charSet[j] +"] = " + new String(originalStr.getBytes(charSet[i]), charSet[j]));
                            Log.i(TAG, "[" +  null +"," + charSet[j] +"] = " + new String(originalStr.getBytes(), charSet[j]));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }


                String test1;

                try {
                    test1 = new String(originalStr.getBytes("euc-kr"  ), "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



                byte[] byMsg = headers.get(0).getBytes();
                Charset euckrCharset = Charset.forName("euc-kr");
                CharBuffer charBuffer = euckrCharset.decode(ByteBuffer.wrap(byMsg));
                String strMsg = charBuffer.toString();
                Log.i(TAG, "euc-kr Convert------------ : " + strMsg);
                Log.i(TAG, "euc-kr Convert------------ : " + strMsg);
                Log.i(TAG, "euc-kr Convert------------ : " + strMsg);

                try {
                    icyHeaders.name = new String(headers.get(0).toString().getBytes("EUC-KR"),"UTF-8");
                    Log.i(TAG, "euc-kr Convert : " + icyHeaders.name);
                }catch(UnsupportedEncodingException e) {
                    Log.i(TAG, "euc-kr Convert Error ");
                    icyHeaders.name = headers.get(0);
                }



            }
            headers = responseHeaders.get(RESPONSE_HEADER_ICY_URL_KEY);
            if (headers != null && headers.size() == 1) {
                icyHeaders.url = headers.get(0);
            }
            headers = responseHeaders.get(RESPONSE_HEADER_ICY_PUB_KEY);
            if (headers != null && headers.size() == 1) {
                icyHeaders.isPublic = headers.get(0).equals("1");
            }
            headers = responseHeaders.get(RESPONSE_HEADER_ICY_METAINT_KEY);
            if (headers != null && headers.size() == 1) {
                metaDataIntervalInBytes = Integer.parseInt(headers.get(0));
                remainingStreamDataUntilMetaDataBlock = metaDataIntervalInBytes;
            }

            if (icyHeadersListener != null) {
                icyHeadersListener.onIcyHeaders(icyHeaders);
            }
        }
        return bytesToRead;
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws HttpDataSourceException {
        int bytesRead;

        // Only read metadata if the server declared to send it...
        if (metaDataIntervalInBytes < 0) {
            bytesRead = super.read(buffer, offset, readLength);
        } else {
            bytesRead = super.read(buffer, offset, remainingStreamDataUntilMetaDataBlock < readLength ? remainingStreamDataUntilMetaDataBlock : readLength);
            if (remainingStreamDataUntilMetaDataBlock == bytesRead) {
                parseIcyMetadata();
            } else {
                remainingStreamDataUntilMetaDataBlock -= bytesRead;
            }
        }
        return bytesRead;
    }

    private void parseIcyMetadata() throws HttpDataSourceException {
        // We hit the metadata block, reset stream data counter
        remainingStreamDataUntilMetaDataBlock = metaDataIntervalInBytes;

        byte[] metaDataBuffer = new byte[1];
        int bytesRead = super.read(metaDataBuffer, 0, 1);
        if (bytesRead != 1) {
            throw new HttpDataSourceException("parseIcyMetadata: Unable to read metadata length!", dataSpec, HttpDataSourceException.TYPE_READ);
        }
        int metaDataBlockSize = metaDataBuffer[0];
        if (metaDataBlockSize < 1) { // Either no metadata or end of file
            return;
        }
        metaDataBlockSize <<= 4; // Multiply by 16 to get actual size

        if (metaDataBuffer.length < metaDataBlockSize) {
            metaDataBuffer = new byte[metaDataBlockSize]; // Make room for the full metadata block
        }

        // Read entire metadata block into buffer
        int offset = 0;
        int readLength = metaDataBlockSize;
        while (readLength > 0 && (bytesRead = super.read(metaDataBuffer, offset, readLength)) != -1) {
            offset += bytesRead;
            readLength -= bytesRead;
        }
        metaDataBlockSize = offset;

        // We read the metadata from the stream. Only parse it when we have a listener registered
        // to return the contents.
        if (icyMetadataListener != null) {
            // Find null-terminator
            for (int i = 0; i < metaDataBlockSize; i++) {
                if (metaDataBuffer[i] == 0) {
                    metaDataBlockSize = i;
                    break;
                }
            }

            try {
                //final String metaDataString = new String(metaDataBuffer, 0, metaDataBlockSize, "utf-8");
                final String metaDataString = new String(metaDataBuffer, 0, metaDataBlockSize, "euc-kr");
                icyMetadataListener.onIcyMetaData(parseMetadata(metaDataString));
            } catch (Exception e) {
                Log.e(TAG, "parseIcyMetadata: Cannot convert bytes to String");
            }
        }
    }

    private IcyMetadata parseMetadata(final String metaDataString) {
        String[] keyAndValuePairs = metaDataString.split(";");
        IcyMetadata icyMetadata = new IcyMetadata();

        for (String keyValuePair : keyAndValuePairs) {
            int equalSignPosition = keyValuePair.indexOf('=');
            if (equalSignPosition < 1) continue;

            boolean isString = equalSignPosition + 1 < keyValuePair.length()
                    && keyValuePair.charAt(keyValuePair.length() - 1) == '\''
                    && keyValuePair.charAt(equalSignPosition + 1) == '\'';

            String key = keyValuePair.substring(0, equalSignPosition);
            String value = isString ?
                    keyValuePair.substring(equalSignPosition + 2, keyValuePair.length() - 1) :
                    equalSignPosition + 1 < keyValuePair.length() ?
                            keyValuePair.substring(equalSignPosition + 1) : "";

            switch (key) {
                case ICY_METADATA_STREAM_TITLE_KEY:
                    icyMetadata.streamTitle = value;
                case ICY_METADATA_STREAM_URL_KEY:
                    icyMetadata.streamUrl = value;
            }
        }

        return icyMetadata;
    }

    public final static class Builder {
        private String userAgent;
        private Predicate<String> contentTypePredicate;
        private TransferListener<? super DefaultHttpDataSource> listener;
        private int connectTimeoutMillis;
        private int readTimeoutMillis;
        private boolean allowCrossProtocolRedirects;
        private RequestProperties defaultRequestProperties;
        private IcyHeadersListener icyHeadersListener;
        private IcyMetadataListener icyMetadataListener;

        public Builder(final String userAgent) {
            this.userAgent = userAgent;
        }

        public Builder setContentTypePredicate(final Predicate<String> contentTypePredicate) {
            this.contentTypePredicate = contentTypePredicate;
            return this;
        }

        public Builder setTransferListener(final TransferListener<? super DataSource> listener) {
            this.listener = listener;
            return this;
        }

        public Builder setConnectTimeoutMillis(final int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder setReadTimeoutMillis(final int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        public Builder setAllowCrossProtocolRedirects(final boolean allowCrossProtocolRedirects) {
            this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
            return this;
        }

        public Builder setDefaultRequestProperties(final RequestProperties defaultRequestProperties) {
            this.defaultRequestProperties = defaultRequestProperties;
            return this;
        }

        public Builder setIcyHeadersListener(final IcyHeadersListener icyHeadersListener) {
            this.icyHeadersListener = icyHeadersListener;
            return this;
        }

        public Builder setIcyMetadataListener(final IcyMetadataListener icyMetadataListener) {
            this.icyMetadataListener = icyMetadataListener;
            return this;
        }

        IcyHttpDataSource build() {
            final IcyHttpDataSource dataSource =
                    new IcyHttpDataSource(userAgent,
                            contentTypePredicate,
                            listener,
                            connectTimeoutMillis,
                            readTimeoutMillis,
                            allowCrossProtocolRedirects,
                            defaultRequestProperties);
            dataSource.icyHeadersListener = icyHeadersListener;
            dataSource.icyMetadataListener = icyMetadataListener;
            return dataSource;
        }
    }

    /**
     * Container for Icy headers such as stream genre or name.
     */
    public final class IcyHeaders {
        /**
         * icy-br
         * Bit rate in KB/s
         */
        int bitRate;
        /**
         * icy-genre
         */
        String genre;
        /**
         * icy-name
         */
        String name;
        /**
         * icy-url
         */
        String url;
        /**
         * icy-pub
         */
        boolean isPublic;

        public int getBitRate() {
            return bitRate;
        }

        public String getGenre() {
            return genre;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public boolean isPublic() {
            return isPublic;
        }

        @Override
        public String toString() {
            return "IcyHeaders{" +
                    "bitRate='" + bitRate + '\'' +
                    ", genre='" + genre + '\'' +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", isPublic=" + isPublic +
                    '}';
        }
    }

    /**
     * Container for stream title and URL.
     *
     * The exact contents isn't specified and implementation specific. It's therefore up to the
     * user to figure what format a given stream returns.
     */
    public final class IcyMetadata {
        String streamTitle;
        String streamUrl;

        public String getStreamTitle() {
            return streamTitle;
        }

        public String getStreamUrl() {
            return streamUrl;
        }

        @Override
        public String toString() {
            return "IcyMetadata{" +
                    "streamTitle='" + streamTitle + '\'' +
                    ", streamUrl='" + streamUrl + '\'' +
                    '}';
        }
    }
}
