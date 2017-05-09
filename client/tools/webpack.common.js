module.exports = {
  module: {
    loaders: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules|bower_components/,
        loader: 'babel'
      },
      {
        test: /\.txt$/,
        exclude: /(node_modules|bower_components)/,
        loader: 'raw'
      },
      {
        test: /\.json$/, loader: 'json'
      },
      {
        test: /\.(ico|gif|png|jpg|jpeg|webp)$/,
        loader: "url?limit=10000&name=./images/[hash].[ext]",
        exclude: /node_modules/
      },
      {
        test: /\.svg(\?v=[0-9\.]+)?$/,
        loader: "url?limit=10000&mimetype=image/svg+xml&name=./images/[hash].[ext]"
      },
      {
        test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"
      },
      {
        test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"
      },
      {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url?limit=10000&mimetype=application/octet-stream&name=./fonts/[hash].[ext]"
      },
      {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
        loader: 'file?prefix=fonts/&limit=10000&mimetype=font/opentype&name=./fonts/[hash].[ext]'
      }
    ]
  },
  progress: true,
  resolve: {
    modulesDirectories: [
      "src",
      "sass",
      "public",
      "node_modules"
    ],
    extensions: ["", ".json", ".js"]
  }
};
