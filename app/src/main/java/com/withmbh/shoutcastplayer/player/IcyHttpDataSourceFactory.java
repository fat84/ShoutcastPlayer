package com.withmbh.shoutcastplayer.player;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

/** A {@link HttpDataSource.Factory} that produces {@link IcyHttpDataSource} instances. */
public final class IcyHttpDataSourceFactory extends HttpDataSource.BaseFactory {
    private String userAgent;
    private TransferListener<? super DataSource> listener;
    private int connectTimeoutMillis;
    private int readTimeoutMillis;
    private boolean allowCrossProtocolRedirects;
    private IcyHttpDataSource.IcyHeadersListener icyHeadersListener;
    private IcyHttpDataSource.IcyMetadataListener icyMetadataListener;

    private IcyHttpDataSourceFactory() {
        // See class Builder
    }

    /**
     * Constructs a IcyHttpDataSourceFactory.
     */
    public final static class Builder {
        private final IcyHttpDataSourceFactory factory;

        /**
         * Sets {@link
         * IcyHttpDataSource#DEFAULT_CONNECT_TIMEOUT_MILLIS} as the connection timeout, {@link
         * IcyHttpDataSource#DEFAULT_READ_TIMEOUT_MILLIS} as the read timeout and disables
         * cross-protocol redirects.
         *
         * @param userAgent The user agent
         */
        public Builder(final String userAgent) {
            // Apply defaults
            factory = new IcyHttpDataSourceFactory();
            factory.userAgent = userAgent;
            factory.listener = null;
            factory.connectTimeoutMillis = IcyHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS;
            factory.readTimeoutMillis = IcyHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS;
            factory.allowCrossProtocolRedirects = false;
        }

        public Builder setTransferListener(final TransferListener<? super DataSource> listener) {
            factory.listener = listener;
            return this;
        }

        public Builder setConnectTimeoutMillis(final int connectTimeoutMillis) {
            factory.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder setReadTimeoutMillis(final int readTimeoutMillis) {
            factory.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        public Builder setAllowCrossProtocolRedirects(final boolean allowCrossProtocolRedirects) {
            factory.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
            return this;
        }

        public Builder setIcyHeadersListener(final IcyHttpDataSource.IcyHeadersListener icyHeadersListener) {
            factory.icyHeadersListener = icyHeadersListener;
            return this;
        }

        public Builder setIcyMetadataChangeListener(final IcyHttpDataSource.IcyMetadataListener icyMetadataListener) {
            factory.icyMetadataListener = icyMetadataListener;
            return this;
        }

        public IcyHttpDataSourceFactory build() {
            return factory;
        }
    }

    @Override
    protected IcyHttpDataSource createDataSourceInternal(HttpDataSource.RequestProperties defaultRequestProperties) {
        return new IcyHttpDataSource.Builder(userAgent)
                .setTransferListener(listener)
                .setConnectTimeoutMillis(connectTimeoutMillis)
                .setReadTimeoutMillis(readTimeoutMillis)
                .setAllowCrossProtocolRedirects(allowCrossProtocolRedirects)
                .setDefaultRequestProperties(defaultRequestProperties)
                .setIcyHeadersListener(icyHeadersListener)
                .setIcyMetadataListener(icyMetadataListener)
                .build();
    }
}
