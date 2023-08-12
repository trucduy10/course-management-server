package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.SubcribesDto;
import com.aptech.coursemanagementserver.models.Author;
import com.aptech.coursemanagementserver.models.Subcribes;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.AuthorRepository;
import com.aptech.coursemanagementserver.repositories.SubcribesRepository;
import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.aptech.coursemanagementserver.services.SubcribesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcribesServiceImpl implements SubcribesService {
    private final SubcribesRepository subcribesRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    @Override
    public SubcribesDto findById(long id) {
        Subcribes subcribes = subcribesRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        "This subcribes with subcribesId: [" + id + "] is not exist."));

        return toDto(subcribes);
    }

    @Override
    public List<SubcribesDto> findByUserId(long userId) {
        List<Subcribes> subcribess = subcribesRepository.findByUserId(userId);

        subcribess.stream()
                // Sort by Ordered then Created_at
                .sorted(Comparator.comparing(Subcribes::getCreated_at).reversed())
                .collect(Collectors.toList());

        List<SubcribesDto> subcribesDtos = new ArrayList<>();

        for (Subcribes subcribes : subcribess) {
            SubcribesDto subcribesDto = toDto(subcribes);
            subcribesDtos.add(subcribesDto);
        }

        return subcribesDtos;
    }

    @Override
    public List<SubcribesDto> findByAuthorId(long authorId) {
        List<Subcribes> subcribesList = subcribesRepository.findByAuthorId(authorId);
        return subcribesList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void subcribe(SubcribesDto subcribesDto) {
        Subcribes subcribes = new Subcribes();
        Author author = authorRepository.findById(subcribesDto.getAuthorId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This author with authorId: [" + subcribesDto.getAuthorId() + "] is not exist."));

        User user = userRepository.findById(subcribesDto.getUserId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This user with userId: [" + subcribesDto.getUserId() + "] is not exist."));

        Optional<Subcribes> checkSubs = subcribesRepository.findByAuthorIdAndUserId(author.getId(), user.getId());

        if (checkSubs.isPresent()) {
            return;
        }

        subcribes.setAuthor(author);
        subcribes.setUser(user);
        subcribesRepository.save(subcribes);
    }

    @Override
    public void unSubcribes(long authorId, long userId) {
        Optional<Subcribes> subcribes = subcribesRepository.findByAuthorIdAndUserId(authorId, userId);
        if (subcribes.isPresent()) {
            subcribesRepository.delete(subcribes.get());
        }
    }

    private SubcribesDto toDto(Subcribes subcribes) {
        SubcribesDto subcribesDto = SubcribesDto.builder()
                .id(subcribes.getId())
                .userId(subcribes.getUser().getId())
                .authorId(subcribes.getAuthor().getId())
                .authorName(subcribes.getAuthor().getName())
                .image(subcribes.getAuthor().getImage())
                .created_at(subcribes.getCreated_at())
                .build();
        return subcribesDto;
    }
}
