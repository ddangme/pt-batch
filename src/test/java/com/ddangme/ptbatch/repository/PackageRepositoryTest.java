package com.ddangme.ptbatch.repository;

import com.ddangme.ptbatch.repository.packaze.PackageEntity;
import com.ddangme.ptbatch.repository.packaze.PackageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PackageRepositoryTest {

    private final PackageRepository packageRepository;

    public PackageRepositoryTest(@Autowired PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @DisplayName("PackageEntity 저장 동작 확인")
    @Test
    void saveTest() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        // When
        packageRepository.save(packageEntity);

        // Then
        assertThat(packageEntity.getPackageSeq()).isNotNull();
    }


    @DisplayName("PackageEntity 조회 동작 확인")
    @Test
    void findByCreatedAtAfterTest() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 3개월");
        packageEntity0.setPeriod(90);
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(100);
        packageRepository.save(packageEntity1);

        // When
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        // Then
        assertThat(packageEntities.size()).isEqualTo(1);
        assertThat(packageEntity1.getPackageSeq()).isEqualTo(packageEntities.get(0).getPackageSeq());
    }

    @DisplayName("PackageEntity 수정 동작 확인")
    @Test
    void updateCountAndPeriodTest() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        // When
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        // Then
        assertThat(updatedCount).isEqualTo(1);
        assertThat(updatedPackageEntity.getCount()).isEqualTo(30);
        assertThat(updatedPackageEntity.getPeriod()).isEqualTo(120);
    }

    @DisplayName("PackageEntity 삭제 동작 확인")
    @Test
    void deleteTest() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);

        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        // When
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        // Then
        assertThat(packageRepository.findById(newPackageEntity.getPackageSeq())).isEmpty();
    }
}
