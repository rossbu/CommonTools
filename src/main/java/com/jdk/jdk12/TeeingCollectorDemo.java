package com.jdk.jdk12;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeeingCollectorDemo {
    public static void main(String[] args) {
        var result =
                Stream.of(
                                // Guest(String name, boolean participating, Integer participantsNumber)
                                new Guest("Marco", true, 3),
                                new Guest("David", false, 2),
                                new Guest("Roger",true, 6))
                        .collect(Collectors.teeing(
                                // first collector, we select only who confirmed the participation
                                Collectors.filtering(Guest::isParticipating,
                                        // whe want to collect only the first name in a list
                                        Collectors.mapping(o -> o.name, Collectors.toList())),
                                // second collector, we want the total number of participants
                                Collectors.summingInt(Guest::getParticipantsNumber),
                                // we merge the collectors in a new Object,
                                // the values are implicitly passed
                                EventParticipation::new
                        ));

        System.out.println(result);

        // Result
        // EventParticipation { guests = [Marco, Roger],
        // total number of participants = 11 }
    }
}
class Guest {
    String name;
    private boolean participating;
    private Integer participantsNumber;

    public Guest(String name, boolean participating,
                 Integer participantsNumber) {
        this.name = name;
        this.participating = participating;
        this.participantsNumber = participantsNumber;
    }

    public boolean isParticipating() {
        return participating;
    }

    public Integer getParticipantsNumber() {
        return participantsNumber;
    }
}

class EventParticipation {
    private List<String> guestNameList;
    private Integer totalNumberOfParticipants;

    public EventParticipation(List<String> guestNameList,
                              Integer totalNumberOfParticipants) {
        this.guestNameList = guestNameList;
        this.totalNumberOfParticipants = totalNumberOfParticipants;
    }

    @Override
    public String toString() {
        return "EventParticipation { " +
                "guests = " + guestNameList +
                ", total number of participants = " + totalNumberOfParticipants +
                " }";
    }}

