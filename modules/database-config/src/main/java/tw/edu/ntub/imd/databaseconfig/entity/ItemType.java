package tw.edu.ntub.imd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.databaseconfig.Config;
import tw.edu.ntub.imd.databaseconfig.converter.BooleanTo1And0Converter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item_type", schema = Config.DATABASE_NAME)
@SuppressWarnings("unused")
public class ItemType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", length = 70, nullable = false)
    private String name;
    @Column(name = "enable", nullable = false)
    @Convert(converter = BooleanTo1And0Converter.class)
    private Boolean enable = true;
    @Column(name = "create_id", length = 100, nullable = false)
    private String createId;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "modify_id", length = 100, nullable = false)
    private String modifyId;
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;
}
