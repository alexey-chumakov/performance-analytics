package com.ghx.hackaton.analytics.stats;

import com.ghx.hackaton.analytics.model.dto.RequestDuration;

import java.util.*;

/**
 * Created by achumakov on 7/20/2015.
 */
public class Sampler {

    public static List<Sample> sample(List<RequestDuration> durations, SamplingFunction samplingFunction) {
        Iterator<RequestDuration> it = durations.iterator();
        Map<Long, Sample> samplingMap = new HashMap<Long, Sample>();

        while (it.hasNext()) {
            RequestDuration requestDuration = it.next();
            Long sampleId = samplingFunction.getSampleId(requestDuration.getTimestamp());
            Sample sample = samplingMap.get(sampleId);
            if (sample == null) {
                sample = new Sample(sampleId);
                samplingMap.put(sampleId, sample);
            }
            sample.addValue(requestDuration);
        }

        List<Sample> result = new ArrayList<Sample>(samplingMap.values());
        Collections.sort(result, new Comparator<Sample>() {
            public int compare(Sample o1, Sample o2) {
                return o1.sampleId.compareTo(o2.getSampleId());
            }
        });

        return result;
    }

    public interface SamplingFunction {
        long getSampleId(long timestamp);
    }

    public static class CustomSamplingFunction implements Sampler.SamplingFunction {

        private long roundTo;

        /**
         * Any rounding parameter can be passed in ms, i.e. 1000 for second, 60000 for minute, etc.
         *
         * @param roundTo rounding parameter in milliseconds
         */
        public CustomSamplingFunction(long roundTo) {
            this.roundTo = roundTo;
        }

        public long getSampleId(long timestamp) {
            return timestamp / roundTo * roundTo;
        }
    }

    public static class Sample {

        private Long sampleId;
        private List<RequestDuration> values = new ArrayList<RequestDuration>();
        private Long totalSum;
        private Long sampleCount;

        public Sample(Long sampleId) {
            this.sampleId = sampleId;
        }

        public void addValue(RequestDuration requestDuration) {
            values.add(requestDuration);
            totalSum += requestDuration.getDuration();
            sampleCount += requestDuration.getCount();
        }

        public Long getSampleId() {
            return sampleId;
        }

        public Double getAverage() {
            return sampleCount > 0 ? ((double) totalSum) / sampleCount : null;
        }
    }

}
