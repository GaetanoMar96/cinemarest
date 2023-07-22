package com.project.cinemarest.factory;

public class PriceCalculatorFactory  {
    private static final double LOW_PRICE = 5.5;
    private static final double MID_PRICE = 7.5;
    private static final double STANDARD_PRICE = 10;

    private PriceCalculatorFactory () {}

    public static TicketPriceCalculator createPriceCalculator(Integer age, Boolean isStudent) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }

        if (age <= 18) {
            return new LowPriceCalculator();
        } else if (Boolean.TRUE.equals(isStudent) && age < 26) {
            return new MidPriceCalculator();
        } else {
            return new StandardPriceCalculator();
        }
    }

    private static class LowPriceCalculator implements TicketPriceCalculator {
        @Override
        public double calculateTicketPrice(Integer age) {
            return LOW_PRICE;
        }
    }

    private static class MidPriceCalculator implements TicketPriceCalculator {
        @Override
        public double calculateTicketPrice(Integer age) {
            return MID_PRICE;
        }
    }

    private static class StandardPriceCalculator implements TicketPriceCalculator {
        @Override
        public double calculateTicketPrice(Integer age) {
            return STANDARD_PRICE;
        }
    }
}
