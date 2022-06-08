package com.company.enroller.controllers;


import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;

    // Pobieranie listy wszystkich spotkań
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    // Pobieranie pojedynczego spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
            if (meeting == null) {
            return new ResponseEntity("Unable to find. A meeting with id " + id + " doesn't exist.",HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    // Dodawanie spotkania
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        Long newId = meeting.getId();

            if (meetingService.findById(newId) == null) {
                meetingService.create(meeting);
                return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
            }
        return new ResponseEntity<String>("Unable to create a meeting. A meeting with id " + newId + " already exists.", HttpStatus.CONFLICT);
    }

    // Dodawanie uczestnika do spotkania
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.PUT)
    public ResponseEntity<?> addParticipantToMeeting(@RequestBody Participant participant, @PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        String login = participant.getLogin();
        Participant participantCheck = participantService.findByLogin(login);

            if (meeting == null) {
                return new ResponseEntity<String>("Unable to find. A meeting with id " + id + " doesn't exist.",HttpStatus.NOT_FOUND);
            }
            if (participantCheck == null) {
                return new ResponseEntity<String>("Unable to update. User " + login + " not found.",HttpStatus.NOT_FOUND);
            }

        meetingService.addParticipantToMeeting(meeting, participant);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    // Pobieranie listy uczestników spotkania

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantList(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<String>("Unable to find. A meeting with id " + id + " doesn't exist.",HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participantsInMeeting = meeting.getParticipants();
        return new ResponseEntity<Collection<Participant>>(participantsInMeeting, HttpStatus.OK);
    }

    // Usuwanie spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.removeMeeting(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.NO_CONTENT);
    }



//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> removeParticipant(@PathVariable("id") String login) {
//        Participant participant = meetingService.findByLogin(login);
//        if (participant == null) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        meetingService.remove(participant);
//        return new ResponseEntity<Participant>(participant, HttpStatus.NO_CONTENT);
//    }
//
//
//
}
