package com.ivygames.common.progress;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class ProgressSerializationTest {

    private static final String RANK_1 = "{\"rank\":1}";

    @Test
    public void SuccessfulSerialization() throws Exception {
        String json = ProgressSerialization.toJson(1);

        Assert.assertThat(json, is(RANK_1));
    }

    @Test
    public void SuccessfulDeSerialization() throws Exception {
        int rank = ProgressSerialization.fromJson(RANK_1);

        Assert.assertThat(rank, is(1));
    }

    @Test
    public void ParseInvalidProgress1() throws Exception {
        int progress = ProgressSerialization.parseProgress("[rank=1575175]");

        Assert.assertThat(progress, is(1575175));
    }

    @Test
    public void ParseInvalidProgress2() throws Exception {
        int progress = ProgressSerialization.parseProgress("Progress [mRank=54397]");

        Assert.assertThat(progress, is(54397));
    }

}