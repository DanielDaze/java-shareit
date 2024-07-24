package ru.practicum.shareit.item.comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setItemId(comment.getItem().getId());
        dto.setId(comment.getId());
        return dto;
    }
}
