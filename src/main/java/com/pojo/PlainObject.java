package com.pojo;

import java.util.Objects;

public class PlainObject {
    private final String name;
    private final String age;
    private final boolean isMale;
    private final String job;

    private PlainObject(final Builder builder) {
        builder.validate();
        this.name = builder.name;
        this.age = builder.age;
        this.isMale = builder.isMale;
        this.job = builder.job;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * @return isMale
     */
    public boolean getIsMale() {
        return isMale;
    }

    /**
     * @return isMale
     */
    public boolean isIsMale() {
        return isMale;
    }

    /**
     * @return job
     */
    public String getJob() {
        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\",\"age\":\"" + age + "\",\"isMale\":" + isMale + ",\"job\":\"" + job + "\"}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(age);
        result = 31 * result + (isMale ? 1 : 0);
        result = 31 * result + Objects.hashCode(job);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlainObject)) {
            return false;
        }
        final PlainObject obj = (PlainObject) o;
        if (!(name == obj.name || (name != null && name.equals(obj.name)))) {
            return false;
        }
        if (!(age == obj.age || (age != null && age.equals(obj.age)))) {
            return false;
        }
        if (isMale != obj.isMale) {
            return false;
        }
        if (!(job == obj.job || (job != null && job.equals(obj.job)))) {
            return false;
        }
        return true;
    }

    public static final class Builder {
        private String name;
        private String age;
        private boolean isMale;
        private String job;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        /**
         * @param name
         */
        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @param age
         */
        public Builder withAge(final String age) {
            this.age = age;
            return this;
        }

        /**
         * @param isMale
         */
        public Builder withIsMale(final boolean isMale) {
            this.isMale = isMale;
            return this;
        }

        /**
         * @param job
         */
        public Builder withJob(final String job) {
            this.job = job;
            return this;
        }

        public PlainObject build() {
            return new PlainObject(this);
        }

        private void validate() {
        }
    }
}
