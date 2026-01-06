import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/utils/app_utils.dart';

class PhoneTextField extends StatefulWidget {
  final String? label;
  final String? hint;
  final String? initialValue;
  final TextEditingController? controller;
  final String? Function(String?)? validator;
  final void Function(String)? onChanged;

  const PhoneTextField({
    super.key,
    this.label,
    this.hint,
    this.initialValue,
    this.controller,
    this.validator,
    this.onChanged,
  });

  @override
  State<PhoneTextField> createState() => _PhoneTextFieldState();
}

class _PhoneTextFieldState extends State<PhoneTextField> {
  late TextEditingController _controller;
  String _displayValue = '';

  @override
  void initState() {
    super.initState();
    _controller = widget.controller ?? TextEditingController();
    if (widget.initialValue != null) {
      _controller.text = widget.initialValue!;
      _updateDisplayValue(_controller.text);
    }
    _controller.addListener(_onTextChanged);
  }

  @override
  void dispose() {
    if (widget.controller == null) {
      _controller.dispose();
    } else {
      _controller.removeListener(_onTextChanged);
    }
    super.dispose();
  }

  void _onTextChanged() {
    _updateDisplayValue(_controller.text);
  }

  void _updateDisplayValue(String value) {
    final cleaned = value.replaceAll(RegExp(r'\D'), '');
    if (cleaned.length <= 9) {
      String formatted = '';
      if (cleaned.isNotEmpty) {
        formatted = '+994 ';
        if (cleaned.length > 0) {
          formatted += cleaned.substring(0, cleaned.length > 2 ? 2 : cleaned.length);
        }
        if (cleaned.length > 2) {
          formatted += ' ';
          formatted += cleaned.substring(2, cleaned.length > 5 ? 5 : cleaned.length);
        }
        if (cleaned.length > 5) {
          formatted += ' ';
          formatted += cleaned.substring(5, cleaned.length > 7 ? 7 : cleaned.length);
        }
        if (cleaned.length > 7) {
          formatted += ' ';
          formatted += cleaned.substring(7);
        }
      }
      setState(() {
        _displayValue = formatted;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: _controller,
      keyboardType: TextInputType.phone,
      inputFormatters: [
        FilteringTextInputFormatter.digitsOnly,
        LengthLimitingTextInputFormatter(9),
      ],
      validator: widget.validator,
      onChanged: (value) {
        widget.onChanged?.call(value.replaceAll(RegExp(r'\D'), ''));
      },
      decoration: InputDecoration(
        labelText: widget.label ?? 'Mobil nömrə',
        hintText: widget.hint ?? '+994 XX XXX XX XX',
        prefixIcon: const Icon(
          Icons.phone,
          color: AppTheme.hintColor,
        ),
        prefixText: '+994 ',
        prefixStyle: TextStyle(
          color: AppTheme.textColor,
          fontWeight: FontWeight.w500,
        ),
      ),
    );
  }
}



