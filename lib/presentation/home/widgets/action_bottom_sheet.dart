import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/theme/app_text_styles.dart';
import '../../../core/localization/app_localizations_ext.dart';

class ActionBottomSheet extends StatelessWidget {
  final int currentIndex;
  final Function(int) onTabSelected;

  const ActionBottomSheet({
    super.key,
    required this.currentIndex,
    required this.onTabSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 96.h,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(32.r),
          topRight: Radius.circular(32.r),
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 10,
            offset: const Offset(0, -2),
          ),
        ],
      ),
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          // Main content
          Padding(
            padding: EdgeInsets.only(
              left: 20.w,
              right: 24.w,
              top: 12.h,
              bottom: 16.h,
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                // Home button
                Expanded(
                  child: _buildTabItem(
                    context,
                    iconPath: 'assets/icons/home.svg',
                    label: 'Home',
                    isSelected: currentIndex == 0,
                    onTap: () => onTabSelected(0),
                  ),
                ),
                SizedBox(width: 8.w),
                // My funds button
                Expanded(
                  child: _buildTabItem(
                    context,
                    iconPath: 'assets/icons/funds.svg',
                    label: 'My funds',
                    isSelected: currentIndex == 1,
                    onTap: () => onTabSelected(1),
                  ),
                ),
                SizedBox(width: 8.w),
                // Spacer for center button (55.5w)
                SizedBox(width: 55.5.w),
                SizedBox(width: 8.w),
                // History button
                Expanded(
                  child: _buildTabItem(
                    context,
                    iconPath: 'assets/icons/history.svg',
                    label: 'History',
                    isSelected: currentIndex == 2,
                    onTap: () => onTabSelected(2),
                  ),
                ),
                SizedBox(width: 8.w),
                // More button
                Expanded(
                  child: _buildTabItem(
                    context,
                    iconPath: 'assets/icons/more.svg',
                    label: 'More',
                    isSelected: currentIndex == 3,
                    onTap: () => onTabSelected(3),
                  ),
                ),
              ],
            ),
          ),
          // Center elevated plus button
          Positioned(
            left: MediaQuery.of(context).size.width / 2 - 32.75.w,
            top: -20.h,
            child: GestureDetector(
              onTap: () {
                // Handle plus button action
                onTabSelected(-1); // Use -1 for plus button
              },
              child: Container(
                width: 65.5.w,
                height: 65.5.w,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withValues(alpha: 0.1),
                      blurRadius: 8,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
                child: Builder(
                  builder: (context) {
                    try {
                      // The plus.svg already contains the red circle and plus sign
                      return SvgPicture.asset(
                        'assets/icons/plus.svg',
                        width: 65.5.w,
                        height: 65.5.w,
                        fit: BoxFit.contain,
                      );
                    } catch (e) {
                      // Fallback to container with icon if SVG doesn't exist
                      return Container(
                        decoration: BoxDecoration(
                          color: AppTheme.mainColor,
                          shape: BoxShape.circle,
                        ),
                        child: Center(
                          child: Icon(
                            Icons.add,
                            color: Colors.white,
                            size: 30.sp,
                          ),
                        ),
                      );
                    }
                  },
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTabItem(
    BuildContext context, {
    required String iconPath,
    required String label,
    required bool isSelected,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 4.h),
        constraints: BoxConstraints(
          maxHeight: 68.h, // 96 - 12 (top) - 16 (bottom) = 68
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Builder(
              builder: (context) {
                try {
                  return Container(
                    width: 26.w,
                    height: 26.h,
                    alignment: Alignment.center,
                    child: FittedBox(
                      fit: BoxFit.contain,
                      alignment: Alignment.center,
                      child: SvgPicture.asset(
                        iconPath,
                        width: 30.w,
                        height: 30.h,
                        fit: BoxFit.contain,
                        colorFilter: ColorFilter.mode(
                          isSelected
                              ? AppTheme.mainColor
                              : AppTheme.textSecondaryGray,
                          BlendMode.srcIn,
                        ),
                      ),
                    ),
                  );
                } catch (e) {
                  // Fallback to icon if SVG doesn't exist
                  return Container(
                    width: 26.w,
                    height: 26.h,
                    alignment: Alignment.center,
                    child: FittedBox(
                      fit: BoxFit.contain,
                      child: Icon(
                        Icons.error_outline,
                        size: 30.sp,
                        color: isSelected
                            ? AppTheme.mainColor
                            : AppTheme.textSecondaryGray,
                      ),
                    ),
                  );
                }
              },
            ),
            SizedBox(height: 2.h),
            Flexible(
              child: FittedBox(
                fit: BoxFit.scaleDown,
                child: Text(
                  label,
                  textScaler: const TextScaler.linear(1.0),
                  style: TextStyle(
                    fontFamily: 'Inter',
                    fontSize: 12,
                    fontWeight: FontWeight.w400,
                    height: 1.1,
                    color: isSelected
                        ? AppTheme.mainColor
                        : AppTheme.textSecondaryGray,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
