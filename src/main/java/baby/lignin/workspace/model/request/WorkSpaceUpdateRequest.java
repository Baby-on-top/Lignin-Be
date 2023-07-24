package baby.lignin.workspace.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkSpaceUpdateRequest {
    private Long workspaceId;
    private String name;
    private String image;
}