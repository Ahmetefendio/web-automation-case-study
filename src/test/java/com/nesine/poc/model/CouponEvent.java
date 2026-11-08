package com.nesine.poc.model;

public record CouponEvent(String name, String date) {

    // the card shows the date on two lines ("Bugün\n21:30"), the basket on one
    public static CouponEvent of(String name, String date) {
        return new CouponEvent(name.trim(), date.trim().replaceAll("\\s+", " "));
    }

    // the basket may abbreviate names: "Preussen Munster - Duisburg" -> "Preussen M. - Duisburg"
    public static boolean sameEvent(CouponEvent a, CouponEvent b) {
        return a.date.equals(b.date) && (namesMatch(a.name, b.name) || namesMatch(b.name, a.name));
    }

    private static boolean namesMatch(String full, String shown) {
        String[] fullWords = full.split(" ");
        String[] shownWords = shown.split(" ");
        if (fullWords.length != shownWords.length) {
            return false;
        }
        for (int i = 0; i < fullWords.length; i++) {
            String word = shownWords[i];
            boolean abbreviation = word.endsWith(".") && fullWords[i].startsWith(word.substring(0, word.length() - 1));
            if (!fullWords[i].equals(word) && !abbreviation) {
                return false;
            }
        }
        return true;
    }
}
