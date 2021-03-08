package com.canlihao.lwutil.lis;

public interface DownloadListener {
    void process(long total);
    void success();
    void failed();
}
