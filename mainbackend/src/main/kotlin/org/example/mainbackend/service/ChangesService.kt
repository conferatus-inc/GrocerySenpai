package org.example.mainbackend.service

import org.example.mainbackend.dto.ChangeDto
import org.example.mainbackend.dto.enums.ChangeType
import org.example.mainbackend.dto.toProduct
import org.example.mainbackend.model.User
import org.example.mainbackend.model.toProductDto
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChangesService(
    private val productsService: ProductsService,
) {
    fun makeChanges(
        user: User,
        changes: List<ChangeDto>,
    ) {
        for (change in changes) {
            when (change.changeType) {
                ChangeType.ADD -> productsService.addProductToUser(change.product.toProduct(), user)
                ChangeType.DELETE -> productsService.deleteProductByIdAndUser(change.product.id!!, user)
                ChangeType.EDIT -> productsService.editProduct(change.product.toProduct(), user)
            }
        }
    }

    fun getChanges(
        user: User,
        fromTime: Instant,
    ): List<ChangeDto> {
        val changes = mutableListOf<ChangeDto>()
        for (product in productsService.findAllByUserAndTime(user, fromTime)) {
            changes.add(
                ChangeDto(
                    product = product.toProductDto(),
                    changeType =
                        when (product.isDeleted) {
                            true -> ChangeType.DELETE
                            false -> ChangeType.ADD
                        },
                    changeTime = product.updated,
                ),
            )
        }
        return changes
    }
}
