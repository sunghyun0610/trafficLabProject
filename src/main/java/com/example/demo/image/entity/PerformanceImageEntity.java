package com.example.demo.image.entity;


import com.example.demo.performance.entity.Performance;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "performance_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Column(name = "image_url")
    private String imageUrl;


    private boolean isThumbnail;

    public PerformanceImageEntity(Performance performance, String imageUrl, boolean isThumbnail){
        this.performance = performance;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
    }
}
