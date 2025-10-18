package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Room {
    private Long id;
    private String name;
    private Set<String> members = new HashSet<>();

    public Room(Long id, String name, Set<String> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public Room() {

    }

}
