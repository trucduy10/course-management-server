package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.TagDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Tag;
import com.aptech.coursemanagementserver.repositories.TagRepository;
import com.aptech.coursemanagementserver.services.TagService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagRepository.findAll();
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = toTagDto(tag);
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }

    @Override
    public BaseDto create(TagDto tagDto) {
        try {
            Tag tag = new Tag();
            tag.setId(tagDto.getId())
                    .setName(tagDto.getName())
                    .setCreated_at(Instant.now())
                    .setUpdated_at(Instant.now());
            tagRepository.save(tag);
            return BaseDto.builder().type(AntType.success).message("Create tag successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "The tag with tagId: [" + tagDto.getId() + "] is not exist.");
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public boolean saveAll(List<TagDto> tagsDto) {
        List<Tag> tags = tagsDto.stream().map(tagDto -> findTagByName(tagDto.getName())).collect(Collectors.toList());
        tagRepository.saveAll(tags);
        return true;
    }

    @Override
    public TagDto findById(long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
                () -> new NoSuchElementException("This tag with tagId: [" + tagId + "] is not exist."));
        TagDto tagDto = toTagDto(tag);
        return tagDto;
    }

    @Override
    public BaseDto update(TagDto tagDto) {
        try {
            Tag tag = tagRepository.findById(tagDto.getId()).orElseThrow(() -> new NoSuchElementException(
                    "The tag with tagId: [" + tagDto.getId() + "] is not exist."));

            tag.setName(tagDto.getName())
                    .setUpdated_at(Instant.now());

            tagRepository.save(tag);

            return BaseDto.builder().type(AntType.success).message("Update tag successfully.").build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public BaseDto delete(long tagId) {
        try {
            Tag tag = tagRepository.findById(tagId).orElseThrow(
                    () -> new NoSuchElementException("The tag with tagId: [" + tagId + "] is not exist."));
            tagRepository.delete(tag);
            return BaseDto.builder().type(AntType.success).message("Delete tag successfully.")
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    private TagDto toTagDto(Tag tag) {
        TagDto tagDto = TagDto.builder()
                .id(tag.getId())
                .name(tag.getName()).build();
        return tagDto;
    }
}
